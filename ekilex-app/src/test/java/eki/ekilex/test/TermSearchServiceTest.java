package eki.ekilex.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import eki.common.test.TestEnvInitialiser;
import eki.ekilex.constant.SystemConstant;
import eki.ekilex.data.MeaningsResult;
import eki.ekilex.data.SearchDatasetsRestriction;
import eki.ekilex.service.db.TermSearchDbService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations = "classpath:test-ekilex-app.properties")
@Transactional
public class TermSearchServiceTest implements SystemConstant {

	@Autowired
	private TestEnvInitialiser testEnvInitialiser;

	@Autowired
	private TermSearchDbService termSearchDbService;

	@Before
	public void beforeTest() throws Exception {

		testEnvInitialiser.initDatabase();
	}

	@Test
	public void testSearchTermMeaningsByWord() throws Exception {

		SearchDatasetsRestriction searchDatasetsRestriction = createDefaultSearchDatasetsRestriction();

		String wordWithMetaCharacters = "hall*";
		String resultLang = null;
		boolean fetchAll = true;

		MeaningsResult meaningsResult = termSearchDbService.getMeaningsResult(wordWithMetaCharacters, searchDatasetsRestriction, resultLang, fetchAll, DEFAULT_OFFSET);

		assertEquals("Incorrect count of matches", 20, meaningsResult.getMeaningCount());
	}

	private SearchDatasetsRestriction createDefaultSearchDatasetsRestriction() {
		SearchDatasetsRestriction searchDatasetsRestriction = new SearchDatasetsRestriction();
		searchDatasetsRestriction.setFilteringDatasetCodes(new ArrayList<>());
		searchDatasetsRestriction.setUserPermDatasetCodes(new ArrayList<>());
		searchDatasetsRestriction.setNoDatasetsFiltering(true);
		searchDatasetsRestriction.setAllDatasetsPermissions(true);
		return searchDatasetsRestriction;
	}
}
