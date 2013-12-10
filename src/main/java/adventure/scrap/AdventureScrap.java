package adventure.scrap;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.gov.frameworkdemoiselle.lifecycle.Startup;

public class AdventureScrap {

	private static List<Evento> eventos = new ArrayList<Evento>();

	@Startup
	public static List<Evento> getEventFromAdventureMag() {
		try {
			Document doc = Jsoup.connect("http://www.adventuremag.com.br/corridadeaventura/calendario/").get();
			Elements elements = doc.select("#colunaesquerda table table tr td:last-child a");
			
			for (Element element : elements) {
				
				Document docevento = Jsoup.connect(element.attr("abs:href")).get();
				String nome = docevento.select(".titulo_nomeevento").text();
				String data = docevento.select(".titulo_nomelocal span").text();
				String local = docevento.select(".titulo_nomelocal:not(span)").text().split("-")[1].trim();
				String link = docevento.select("#data a").get(0).attr("abs:href");
				
				Evento e = new Evento(nome, data, local, link);
				eventos.add(e);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}
}
