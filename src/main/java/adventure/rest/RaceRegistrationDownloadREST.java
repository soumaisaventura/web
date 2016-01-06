package adventure.rest;

import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import adventure.persistence.RegistrationDAO;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.util.Reflections;

//@Path("race/{id}/registration")
public class RaceRegistrationDownloadREST {

	@GET
	@LoggedIn
	@Path("form")
	@Produces("application/pdf")
	public byte[] resgistrationForm(@PathParam("id") Integer id) throws Exception {
		Race race = loadRaceDetails(id);

		// List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() /* && !organizers.contains(User.getLoggedIn()) */) {
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

		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(race.getDate());

		return oututStream.toByteArray();
	}

	@GET
	@LoggedIn
	@Path("export")
	@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public byte[] exportXLSX(@PathParam("id") Integer id) throws Exception {
		Race race = loadRaceDetails(id);

		// List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() /* && !organizers.contains(User.getLoggedIn()) */) {
			throw new ForbiddenException();
		}

		Workbook workbook = new XSSFWorkbook();
		// CreationHelper createHelper = workbook.getCreationHelper();

		// CellStyle currencyStyle = workbook.createCellStyle();
		// currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

		CellStyle titleStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();
		titleFont.setBoldweight(BOLDWEIGHT_BOLD);
		titleStyle.setFont(titleFont);

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
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Nascimento");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("RG");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("CPF");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Estado");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Cidade");
		createStyleCell(rAtletas, cAtletasIdx++, titleStyle).setCellValue("Inscrição (R$)");

		rEquipes = sheetEquipes.createRow(rEquipesIdx++);
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Inscrição");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Status");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Percurso");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Categoria");
		createStyleCell(rEquipes, cEquipesIdx++, titleStyle).setCellValue("Equipe");
		int biggestTeam = 0;

		List<Registration> registrations = RegistrationDAO.getInstance().findToOrganizer(race.getEvent());
		Collections.reverse(registrations);

		for (Registration registration : registrations) {
			CellStyle style = workbook.createCellStyle();
			IndexedColors color;
			switch (registration.getStatus()) {
				case CANCELLED:
					color = IndexedColors.RED;
					break;

				case PENDENT:
					color = IndexedColors.DARK_YELLOW;
					break;

				default:
					color = IndexedColors.BLACK;
					break;
			}

			// style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			// style.setFillPattern(CellStyle.SOLID_FOREGROUND);

			Font font = workbook.createFont();
			font.setColor(color.getIndex());
			style.setFont(font);

			rEquipes = sheetEquipes.createRow(rEquipesIdx++);

			cEquipesIdx = 0;
			createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(registration.getFormattedId());
			createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(registration.getStatus().description());
			// createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(
			// registration.getRaceCategory().getCourse().getName());
			createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(
					registration.getRaceCategory().getCategory().getName());
			createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(registration.getTeamName());

			for (UserRegistration teamFormation : registration.getUserRegistrations()) {
				User user = teamFormation.getUser();
				Profile profile = user.getProfile();

				createStyleCell(rEquipes, cEquipesIdx++, style).setCellValue(profile.getName());
				rAtletas = sheetAtletas.createRow(rAtletasIdx++);

				cAtletasIdx = 0;
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(registration.getFormattedId());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(registration.getStatus().description());
				// createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(
				// registration.getRaceCategory().getCourse().getName());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(
						registration.getRaceCategory().getCategory().getName());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(registration.getTeamName());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(profile.getName());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(user.getEmail());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(
						profile.getTshirt() == null ? "" : profile.getTshirt().name());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(profile.getMobile());

				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(Dates.parse(profile.getBirthday()));
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(profile.getRg());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(profile.getCpf());

				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(
						profile.getCity().getState().getAbbreviation());
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(profile.getCity().getName());

				short dataFormat;
				dataFormat = style.getDataFormat();
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
				createStyleCell(rAtletas, cAtletasIdx++, style).setCellValue(teamFormation.getRacePrice().floatValue());
				style.setDataFormat(dataFormat);
			}

			biggestTeam = registration.getUserRegistrations().size() > biggestTeam ? registration
					.getUserRegistrations().size() : biggestTeam;
		}

		for (int i = 0; i < biggestTeam; i++) {
			createStyleCell(sheetEquipes.getRow(0), sheetEquipes.getRow(0).getLastCellNum(), titleStyle).setCellValue(
					"Atleta " + (i + 1));
		}

		autoSizeColumns(sheetAtletas);
		autoSizeColumns(sheetEquipes);

		sheetAtletas.setAutoFilter(CellRangeAddress.valueOf("A1:M1"));
		sheetAtletas.createFreezePane(0, 1);
		// sheetAtletas.createSplitPane(2000, 2000, 0, 0, Sheet.PANE_LOWER_LEFT);

		sheetEquipes.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
		sheetEquipes.createFreezePane(0, 1);
		// sheetAtletas.createSplitPane(2000, 2000, 0, 0, Sheet.PANE_LOWER_LEFT);

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
		// Race result = RaceDAO.getInstance().loadForDetail(id);
		Race result = null;

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
