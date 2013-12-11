package adventure.scrap;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import adventure.entity.Evento;

public class AdventureScrap {

	private static List<Evento> eventos = new ArrayList<Evento>();

	public static List<Evento> getEventFromAdventureMag() {
		List<String> urls = new ArrayList<String>();
		Document doc, doc2, docevento;
		Elements pagination, elements;

		try {
			urls.add("http://www.adventuremag.com.br/corridadeaventura/calendario/index.php?page=1");
			doc = Jsoup.connect(urls.get(0)).timeout(0).get();
			pagination = doc.select(".pagination").get(0).getElementsByTag("a");

			for (int i = 0; i < pagination.size() - 1; i++) {
				urls.add(pagination.get(i).attr("abs:href"));
			}

			for (String url : urls) {
				doc2 = Jsoup.connect(url).get();
				elements = doc2.select("#colunaesquerda table table tr td:last-child a");
				for (Element element : elements) {
					docevento = Jsoup.connect(element.attr("abs:href")).get();
					String nome = docevento.select(".titulo_nomeevento").text();
					//String data = docevento.select(".titulo_nomelocal span").text();
					String local = docevento.select(".titulo_nomelocal:not(span)").text().split("-")[1].trim();
					String link = docevento.select("#data a").get(0).attr("abs:href");
					// System.out.println("-------------------------------------------------------------");
					// System.out.println(nome);
					// System.out.println(data);
					// System.out.println(local);
					// System.out.println(link);
					// Evento e = new Evento(nome, data, local, link);
					Evento e = new Evento();
					e.setNome(nome);
					// e.setData(data);
					e.setLocal(local);
					e.setLink(new URL(link));
					eventos.add(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}
}
