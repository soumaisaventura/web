package adventure.rest;

import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.util.Reflections;

@Path("race/{id}/registration")
public class RaceRegistrationDownloadREST {

	@GET
	@LoggedIn
	@Path("form")
	@Produces("application/pdf")
	public byte[] resgistrationForm(@PathParam("id") Integer id) throws Exception {
		Race race = loadRaceDetails(id);

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		Context context = new InitialContext();
		DataSource dataSource = (DataSource) context.lookup("java:jboss/datasources/PostgreSQLDS");
		Connection conn = dataSource.getConnection();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RACE_ID", race.getId());

		InputStream inputStream = Reflections.getResourceAsStream("report/ficha_inscricao.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, conn);
		conn.close();

		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, oututStream);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(race.getDate());

		return oututStream.toByteArray();
	}

	@GET
	@LoggedIn
	@Path("export")
	@Produces("application/vnd.ms-excel")
	public byte[] exportXLSX(@PathParam("id") Integer id) throws Exception {
		Race race = loadRaceDetails(id);

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		Workbook workbook = new HSSFWorkbook();

		CellStyle currencyStyle = workbook.createCellStyle();
		currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

		CellStyle titleStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(BOLDWEIGHT_BOLD);
		titleStyle.setFont(font);

		Sheet sheetAtletas = workbook.createSheet("Atletas");
		Sheet sheetEquipes = workbook.createSheet("Equipes");

		Row rAtletas, rEquipes;
		int rAtletasIdx = 0, cAtletasIdx = 0;
		int rEquipesIdx = 0, cEquipesIdx = 0;

		rAtletas = sheetAtletas.createRow(rAtletasIdx++);
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Inscrição");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Status");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Percurso");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Categoria");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Equipe");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Nome");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("E-mail");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Camisa");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Telefone");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Estado");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Cidade");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Inscrição (R$)");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Anuidade (R$)");

		rEquipes = sheetEquipes.createRow(rEquipesIdx++);
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Inscrição");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Status");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Percurso");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Categoria");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Equipe");
		int biggestTeam = 0;

		List<Registration> registrations = RegistrationDAO.getInstance().findToOrganizer(race);
		Collections.reverse(registrations);

		for (Registration registration : registrations) {
			rEquipes = sheetEquipes.createRow(rEquipesIdx++);

			cEquipesIdx = 0;
			rEquipes.createCell(cEquipesIdx++).setCellValue(registration.getFormattedId());
			rEquipes.createCell(cEquipesIdx++).setCellValue(registration.getStatus().description());
			rEquipes.createCell(cEquipesIdx++).setCellValue(registration.getRaceCategory().getCourse().getName());
			rEquipes.createCell(cEquipesIdx++).setCellValue(registration.getRaceCategory().getCategory().getName());
			rEquipes.createCell(cEquipesIdx++).setCellValue(registration.getTeamName());

			for (TeamFormation teamFormation : registration.getTeamFormations()) {
				User user = teamFormation.getUser();
				Profile profile = user.getProfile();

				rEquipes.createCell(cEquipesIdx++).setCellValue(profile.getName());
				rAtletas = sheetAtletas.createRow(rAtletasIdx++);

				cAtletasIdx = 0;
				rAtletas.createCell(cAtletasIdx++).setCellValue(registration.getFormattedId());
				rAtletas.createCell(cAtletasIdx++).setCellValue(registration.getStatus().description());
				rAtletas.createCell(cAtletasIdx++).setCellValue(registration.getRaceCategory().getCourse().getName());
				rAtletas.createCell(cAtletasIdx++).setCellValue(registration.getRaceCategory().getCategory().getName());
				rAtletas.createCell(cAtletasIdx++).setCellValue(registration.getTeamName());

				rAtletas.createCell(cAtletasIdx++).setCellValue(profile.getName());
				rAtletas.createCell(cAtletasIdx++).setCellValue(user.getEmail());
				rAtletas.createCell(cAtletasIdx++).setCellValue(
						profile.getTshirt() == null ? "" : profile.getTshirt().name());
				rAtletas.createCell(cAtletasIdx++).setCellValue(profile.getMobile());
				rAtletas.createCell(cAtletasIdx++).setCellValue(profile.getCity().getState().getAbbreviation());
				rAtletas.createCell(cAtletasIdx++).setCellValue(profile.getCity().getName());

				createStyleCell(rAtletas, cAtletasIdx++, currencyStyle).setCellValue(
						teamFormation.getRacePrice().floatValue());

				createStyleCell(rAtletas, cAtletasIdx++, currencyStyle).setCellValue(
						teamFormation.getAnnualFee().floatValue());
			}

			biggestTeam = registration.getTeamFormations().size() > biggestTeam ? registration.getTeamFormations()
					.size() : biggestTeam;
		}

		for (int i = 0; i < biggestTeam; i++) {
			createStyleCell(sheetEquipes.getRow(0), sheetEquipes.getRow(0).getLastCellNum(), titleStyle).setCellValue(
					"Atleta " + (i + 1));
		}

		autoSizeColumns(sheetAtletas);
		autoSizeColumns(sheetEquipes);

		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		workbook.write(oututStream);
		oututStream.close();
		workbook.close();

		return oututStream.toByteArray();
	}

	private void autoSizeColumns(Sheet sheet) {
		for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private Cell createStyleCell(Row row, int column, CellStyle style) {
		Cell cell;

		cell = row.createCell(column);
		cell.setCellStyle(style);

		return cell;
	}

	private Race loadRaceDetails(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetail(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
