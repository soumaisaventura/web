package rest;

import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.business.ImageBusiness;
import core.business.ProfileBusiness;
import core.entity.Picture;
import core.entity.Profile;
import core.entity.User;
import core.persistence.CityDAO;
import core.persistence.ProfileDAO;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import rest.data.CityData;
import rest.data.OrienteeringData;
import rest.data.ProfileData;
import temp.util.PendencyCounter;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static core.util.Constants.USER_PHOTO_WIDTH;
import static core.util.Constants.USER_THUMBNAIL_WIDTH;

@Path("user/profile")
public class UserProfileREST {

    @GET
    @LoggedIn
    @Produces("application/json")
    public ProfileData load() throws Exception {
        Profile profile = loadProfileDetails(User.getLoggedIn());

        ProfileData data = new ProfileData();
        data.name = profile.getName();
        data.rg = profile.getRg();
        data.cpf = profile.getCpf();
        data.birthday = profile.getBirthday();
        data.mobile = profile.getMobile();
        data.gender = profile.getGender();
        data.tshirt = profile.getTshirt();
        data.pendencies = profile.getPendencies();
        data.city = new CityData(profile.getCity());

        if (data.city.id == null) {
            data.city = null;
        }

        data.orienteeringData = new OrienteeringData();
        data.orienteeringData.nationalId = profile.getNationalId();
        data.orienteeringData.sicardNumber = profile.getSicardNumber();

        return data;
    }

    @GET
    @Path("{id: \\d+}/picture")
    @Produces("image/jpeg")
    @Cache("max-age=60")
    public Response getPicture(@PathParam("id") Integer id, @HeaderParam("If-None-Match") String tag,
                               @Context ServletContext context) throws Exception {
        Response response;
        Profile profile = loadProfile(id);
        String persistedTag = profile.getPictureHash();

        if (persistedTag.equals(tag)) {
            response = Response.notModified(persistedTag).build();
        } else {
            byte[] entity = loadPicture(profile, context);
            response = Response.ok(entity).tag(persistedTag).build();
        }

        return response;
    }

    @GET
    @Path("{id: \\d+}/thumbnail")
    @Produces("image/jpeg")
    @Cache("max-age=604800000")
    public Response getThumbnail(@PathParam("id") Integer id, @HeaderParam("If-None-Match") String tag,
                                 @Context ServletContext context) throws Exception {
        Response response;
        Profile profile = loadProfile(id);
        String persistedTag = profile.getPictureHash();

        if (persistedTag.equals(tag)) {
            response = Response.notModified(persistedTag).build();
        } else {
            byte[] entity = ImageBusiness.getInstance().resize(loadPicture(profile, context), USER_PHOTO_WIDTH,
                    USER_THUMBNAIL_WIDTH);
            response = Response.ok(entity).tag(persistedTag).build();
        }

        return response;
    }

    @PUT
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Path("{id: \\d+}/picture")
    @Consumes("image/jpeg")
    public void setPicture(@PathParam("id") Integer id, @NotEmpty InputStream inputStream) throws Exception {
        Profile profile = loadProfile(id);
        checkPermission(profile);

        if (inputStream == null) {
            throw new UnprocessableEntityException().addViolation("arquivo obrigatório");
        }

        ProfileBusiness.getInstance().updatePicture(id, new Picture(inputStream, "image/jpg"));
    }

    private void checkPermission(Profile profile) throws ForbiddenException {
        if (!User.getLoggedIn().getAdmin() && !profile.getUser().getId().equals(User.getLoggedIn().getId())) {
            throw new ForbiddenException();
        }
    }

    @PUT
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    public void update(ProfileData data) throws Exception {
        Profile persisted = loadProfile(User.getLoggedIn());
        persisted.setName(data.name);
        persisted.setRg(data.rg);
        persisted.setCpf(data.cpf);
        persisted.setBirthday(data.birthday);
        persisted.setMobile(data.mobile);
        persisted.setGender(data.gender);
        persisted.setTshirt(data.tshirt);
        persisted.setCity(CityDAO.getInstance().load(data.city.id));
        persisted.setNationalId(data.orienteeringData.nationalId);
        persisted.setSicardNumber(data.orienteeringData.sicardNumber);

        ProfileDAO.getInstance().update(persisted);
        User.getLoggedIn().getProfile().setPendencies(PendencyCounter.count(persisted));
    }

    private Profile loadProfile(Integer id) throws NotFoundException {
        Profile result = ProfileDAO.getInstance().load(id);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private Profile loadProfile(User user) throws NotFoundException {
        Profile result = ProfileDAO.getInstance().load(user);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private Profile loadProfileDetails(User user) throws NotFoundException {
        Profile result = ProfileDAO.getInstance().loadDetails(user);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private byte[] loadPicture(Profile profile, ServletContext context) throws Exception {
        byte[] result = profile.getPicture();

        if (result == null) {
            InputStream in = context.getResourceAsStream(
                    "/images/foto_anonimo_" + profile.getGender().toString().toLowerCase() + ".jpg");
            result = IOUtils.toByteArray(in);
        }

        return result;
    }
}
