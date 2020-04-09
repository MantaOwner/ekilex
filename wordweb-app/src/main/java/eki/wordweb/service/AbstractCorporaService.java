package eki.wordweb.service;

import eki.common.constant.GlobalConstant;
import eki.wordweb.constant.SystemConstant;
import eki.wordweb.data.CorporaSentence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractCorporaService implements SystemConstant, GlobalConstant {

	private static final Logger logger = LoggerFactory.getLogger(AbstractCorporaService.class);

	protected abstract List<CorporaSentence> parseResponse(Map<String, Object> response);

	protected abstract URI composeCorporaUri(String sentence);

	@Cacheable(value = CACHE_KEY_CORPORA)
	public List<CorporaSentence> getSentences(String sentence) {
		Map<String, Object> response = requestSentences(sentence);
		return parseResponse(response);
	}

	private Map<String, Object> requestSentences(String sentence) {

		URI corporaUrl = composeCorporaUri(sentence);
		Map<String, Object> response = Collections.emptyMap();
		if (isNotEnabled(corporaUrl)) {
			return response;
		}

		String responseAsString = doGetRequest(corporaUrl);
		if (responseAsString != null) {
			JsonParser jsonParser = JsonParserFactory.getJsonParser();
			response = jsonParser.parseMap(responseAsString);
		}
		return response;
	}

	protected String parseSentenceToQueryString(String sentence) {
		String[] words = StringUtils.split(sentence, " ");
		if (words.length > 1) {
			List<String> items = new ArrayList<>();
			for (String word : words) {
				items.add("[word=\"" + word + "\" %c]");
			}
			return String.join(" ", items);
		} else {
			return "[lempos=\"" + sentence + "-.?\"]";
		}
	}

	private String doGetRequest(URI url) {

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		RestTemplate restTemplate = new RestTemplate();

		logger.debug("Sending request to > {}", url.toString());
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
		} catch (Exception e) {
			logger.error("Error with requesting {}", url);
			logger.error(e.getMessage());
			return null;
		}
	}

	private boolean isNotEnabled(URI corporaUrl) {
		return corporaUrl == null;
	}

}
