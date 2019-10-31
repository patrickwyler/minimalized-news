package ch.wyler.minimalizednews.utils;

import ch.wyler.minimalizednews.config.Config;
import ch.wyler.minimalizednews.dtos.ArticleFullDto;
import ch.wyler.minimalizednews.dtos.TagDto;
import ch.wyler.minimalizednews.mappers.ArticleMapper;
import ch.wyler.minimalizednews.mappers.TagMapper;
import ch.wyler.minimalizednews.services.impl.ArticleService;
import ch.wyler.minimalizednews.services.impl.TagService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Testdata creator can be enabled/disabled with a config property.
 */
@Slf4j
@Component
@Getter(value = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestdataCreator {

	ArticleService articleService;
	ArticleMapper articleMapper;
	TagService tagService;
	TagMapper tagMapper;
	Config config;

	@Autowired
	public TestdataCreator(final ArticleService articleService, final ArticleMapper articleMapper, final TagService tagService, final TagMapper tagMapper, final Config config) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.tagService = tagService;
		this.tagMapper = tagMapper;
		this.config = config;
	}

	/**
	 * Add testdata automatically while starting up the app
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void setUp() {
		// check if testdata should be created or not
		if (!getConfig().isTestModeEnabled()) {
			return;
		}

		createTestData();
	}

	public void createTestData() {
		log.info("Creating test data...");

		createTags();
		createArticles();

		log.info("Done!");
	}

	private void createArticles() {
		final List<ArticleFullDto> articles = new ArrayList<>();

		articles.add(ArticleFullDto.builder()
				.id(1L)
				.title("Gadget Test - Das Samsung Galaxy A80 übertrifft alle Erwartungen")
				.rawText("Die Kamera des neuen Samsung Galaxy übertrifft alle Erwartungen. Das Handy liest dir jeden Wunsch von den Augen ab und schiesst Fotos, die dich zum " +
						"Insta-Star machen könnten. Das Samsung Galaxy A80 denkt mit. Es spart bei den Apps Batterie, die du am wenigsten brauchst. Ausserdem erkennt es, wenn du " +
						"Auto fährst und kann in diesem Fall automatisch Google Maps starten. Das Handy operiert mit zwei SIM-Karten, wovon beide gleichzeitig aktiviert sein können. " +
						"Du entschiedest, welche Nummer wofür zuständig ist und ob du sie ein- oder ausgeschaltet haben möchtest. Der wahre Star dieses aussergewöhnlichen " +
						"Tech-Gadgets ist aber die Kamera. Genauer gesagt die erste rotierende Triple-Kamera von Samsung. Das integrierte Kameramodul fährt automatisch auf der " +
						"Rückseite des Smartphones aus und dreht sich um 180 Grad nach vorne, wenn du den Selfie-Modus in der Kamera-App auswählst. In der Nachbearbeitung kannst " +
						"du einzelne Gesichter ganz einfach bearbeiten und unerwünschte Schatten, Flecken, Formen oder Farbtöne entfernen. Im Fotomodus hast du dreissig " +
						"verschiedene Modi zur Auswahl.Die Kamera erkennt automatisch die Eigenschaften des gewählten Sujets und passt die Funktionen an. Nicht nur für Selfies, " +
						"auch für Actionszenen ist dieses Handy äusserst geeignet. Egal, ob du einen Kurzfilm mit einer Kampfsport-Szene drehen oder eine Freundin beim Skateboarden " +
						"filmen willst, die Kamera weiss, wies geht. Dank des 48-Megapixel-Weitwinkelobjektivs kannst du eine riesige Bildfläche einfangen und mit der " +
						"Action-Cam-Videostabilisierung bleiben die Bewegungen des Bildes selbst bei schnellen Schwenkern weich und erkennbar. Und weil für Fotografen und Kameramänner " +
						"ausreichend Akkulaufzeit bei ihren Arbeitsgeräten höchste Priorität hat, ist auch die Akkuverwaltung des Handys intelligent, damit du so lange wie möglich ohne " +
						"Ladepause filmen und fotografieren kannst. Das einzige, das dir das Samsung Galaxy A80 nicht abnehmen kann, ist die Wahl eines guten Motivs.")
				.sourceName("Microspot")
				.sourceUrl("https://www.microspot.ch/de/cms/blog/produkttest/mobiltelefon-samsung-galaxy-a80")
				.approved(true)
				.tags(Collections.singleton(TagDto.builder().id(11L).build())).build());

		articles.add(ArticleFullDto.builder()
				.id(2L)
				.title("microspot.ch sogar günstiger als Digitec oder italienischer Amazon Ableger Amazon Italia")
				.rawText("Was zeigen die Resultate der Preiserhebung nun genau? Wirft man einen Blick auf die Ergebnisse des Preisbarometers auf www.preisbarometer.ch, kann man " +
						"sehen, dass ein gleicher Warenkorb bei microspot.ch günstiger ist als bei der direkten Konkurrenz. Mit 5.4 % Preisunterschied ist Brack.ch beispielsweise " +
						"deutlich teurer. Noch grösser werden die Preisunterschiede, wenn man die Vergleiche mit den umliegenden Ländern Frankreich, Deutschland und Italien betrachtet: " +
						"Beim italienischen Amazon Ableger Amazon Italia zahlt man beispielsweise für denselben Warenkorb 8.7 % mehr als beim günstigsten Schweizer Elektronikhändler " +
						"microspot.ch. Noch teurer sind die Warenkörbe bei den deutschen Händlern Mediamarkt (+ 11.7 %) und Conrad (+17.6 %). Übertroffen werden diese allerdings dennoch " +
						"von den französischen Anbietern Rue du Commerce und Pixmania. Bei Pixmania zahlen die Konsumenten sogar 32.1 % mehr pro Warenkorb.")
				.sourceName("20 Minuten")
				.sourceUrl("https://www.microspot.ch/de/cms/blog/events-trends/microspot-ist-der-guenstigste-elektronikhaendler-der-schweiz")
				.approved(true)
				.tags(new HashSet<>(Arrays.asList(TagDto.builder().id(11L).build(), TagDto.builder().id(2L).build(), TagDto.builder().id(16L).build()))).build());

		articles.add(ArticleFullDto.builder()
				.id(3L)
				.title("Der Höhenflug von microspot.ch ist eingeläutet")
				.rawText("Der Himmel über unseren Köpfen löste bei uns Menschen schon immer eine grosse Faszination und ein gewisses Mysterium aus. Seit Jahrtausenden untersuchen " +
						"Forscher die Vorgänge und Eigenschaften dieses scheinbar unendlichen Raumes. Auch heute sind immer noch tausende von Wissenschaftlern damit beschäftigt, " +
						"die Phänomene dieser verschiedenen Schichten zu erforschen und aufzudecken. Team aus zwei Schülerinnen und einem Schüler der Kantonsschule Zofingen hat " +
						"sich dies als Vorbild genommen und wollte im Rahmen ihrer Maturaarbeit unseren Lauftraum untersuchen. Dabei haben sie sich das Ziel gesetzt, bis in die " +
						"Höhen der Stratosphäre vorzustossen und dieser auf den Grund zu gehen. Die derzeit einzig zuverlässige Möglichkeit, Forschungen vom Boden aus in dieser " +
						"grossen Entfernung zu machen, ist anhand eines Stratosphärenballons. Dieser etablierte sich abgesehen von Satelliten als weltweites Hauptmessgerät, um " +
						"Daten zur Wetterprognose in der Höhe der Stratosphäre zu erfassen. Als naturwissenschaftlich begeistertes Projektteam haben sie sich entschieden, nicht " +
						"ein bereits fertiggestelltes Sensoren-Paket zur Datenmessung zu verwenden, sondern selber eines zusammenzustellen und zu programmieren. Dieses Paket wurde " +
						"mithilfe des Ballons in eine Höhe von 30'000 m ü. M. befördert und konnte da spannende Daten über Ozon-, Temperatur-, Druck- und Höhenwerte sammeln. Um diese " +
						"bemerkenswerte Reise für immer festzuhalten, wurde der Stratosphärenballon mit einer GoPro ausgestattet. Diese konnte die gesamte Faszination wie geplant " +
						"aufzeichnen und lieferte eindrückliches Video- sowie Fotomaterial. Sodass alle Wetterdaten und aufregenden Aufnahmen wiedergefunden werden konnten, wurde der " +
						"Ballon im Vorhinein mit einem GPS-Tracker versehen. Dank diesem konnte das Paket geortet und schlussendlich in einer 15 Meter hohen Baumkrone in Schlierbach LU" +
						" geborgen werden. Auf der Suche nach geeigneten Sponsoren für diese essentiellen Geräte ist das junge Forscherteam auf uns, microspot.ch aufmerksam geworden. " +
						"Nach ihrer Anfrage und dem Vorstellen dieses spannenden Projektes, waren wir gleich Feuer und Flamme. Selbstverständlich wollten wir diese engagierten " +
						"Jung-Wissenschaftler mit dem notwendigen Material unterstützen und sie auf dieser Reise begleiten. Als kreatives Dankeschön wurde das microspot.ch Logo am " +
						"Stratosphärenballon befestigt und am Rande der Stratosphäre abgelichtet. So entstanden unglaubliche Aufnahmen, welche uns allen noch sehr lange in Erinnerung " +
						"bleiben werden. Mit dem erweiterten Sortiment wollen wir zwar hoch hinaus, dass es aber in der kurzen Zeit so hoch geht, konnten wir uns in den kühnsten Träumen " +
						"nicht vorstellen.")
				.sourceName("Microspot")
				.sourceUrl("https://www.microspot.ch/de/cms/blog/events-trends/hoehenflug-von-microspot")
				.approved(true)
				.build());

		// create articles
		articles.forEach(articleDto -> getArticleService().create(getArticleMapper().articleFullDtoToArticleModel(articleDto)));

		// use summarize function for generating the texts
		articles.forEach(articleDto -> getArticleService().summarizeById(articleDto.getId()));
	}

	private void createTags() {
		final List<TagDto> tags = new ArrayList<>();

		tags.add(TagDto.builder().id(1L).name("Sport").build());
		tags.add(TagDto.builder().id(2L).name("Finanzen").build());
		tags.add(TagDto.builder().id(3L).name("Umwelt").build());
		tags.add(TagDto.builder().id(4L).name("Wetter").build());
		tags.add(TagDto.builder().id(5L).parentTag(TagDto.builder().id(4L).build()).name("Hitzesommer").build());
		tags.add(TagDto.builder().id(6L).parentTag(TagDto.builder().id(4L).build()).name("Winter").build());
		tags.add(TagDto.builder().id(7L).parentTag(TagDto.builder().id(4L).build()).name("Herbst").build());
		tags.add(TagDto.builder().id(8L).parentTag(TagDto.builder().id(4L).build()).name("Sommer").build());
		tags.add(TagDto.builder().id(9L).parentTag(TagDto.builder().id(4L).build()).name("Frühling").build());
		tags.add(TagDto.builder().id(10L).name("Ausland").build());
		tags.add(TagDto.builder().id(11L).name("IT").build());
		tags.add(TagDto.builder().id(12L).name("Länder").build());
		tags.add(TagDto.builder().id(13L).parentTag(TagDto.builder().id(12L).build()).name("Schweiz").build());
		tags.add(TagDto.builder().id(14L).parentTag(TagDto.builder().id(12L).build()).name("USA").build());
		tags.add(TagDto.builder().id(15L).parentTag(TagDto.builder().id(12L).build()).name("Deutschland").build());
		tags.add(TagDto.builder().id(16L).parentTag(TagDto.builder().id(13L).build()).name("Bern").build());
		tags.add(TagDto.builder().id(17L).parentTag(TagDto.builder().id(13L).build()).name("Zürich").build());
		tags.add(TagDto.builder().id(18L).parentTag(TagDto.builder().id(13L).build()).name("Basel").build());

		// create tags
		tags.forEach(tagDto -> getTagService().create(getTagMapper().tagDtoToTagModel(tagDto)));
	}
}