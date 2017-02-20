package com.gentics.mesh.search;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.gentics.mesh.core.data.search.IndexHandler;
import com.gentics.mesh.dagger.MeshInternal;
import com.gentics.mesh.test.AbstractRestEndpointTest;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public abstract class AbstractSearchEndpointTest extends AbstractRestEndpointTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractSearchEndpointTest.class);

	@BeforeClass
	public static void initMesh() throws Exception {
		init(true);
		initDagger(false);
	}

	@Before
	public void setupHandlers() throws Exception {
		// We need to call init() again in order create missing indices for the created test data
		for (IndexHandler handler : meshDagger.indexHandlerRegistry().getHandlers()) {
			handler.init().await();
		}
	}

	@After
	public void resetElasticSearch() {
		searchProvider.clear();
	}

	@BeforeClass
	@AfterClass
	public static void clean() throws IOException {
		FileUtils.deleteDirectory(new File("data"));
	}

	protected String getSimpleQuery(String text) throws JSONException {
		QueryBuilder qb = QueryBuilders.queryStringQuery(text);
		JSONObject request = new JSONObject();
		request.put("query", new JSONObject(qb.toString()));
		String query = request.toString();
		if (log.isDebugEnabled()) {
			log.debug(query);
		}
		return query;
	}

	protected String getSimpleTermQuery(String key, String value) throws JSONException {
		QueryBuilder qb = QueryBuilders.termQuery(key, value);
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		bqb.must(qb);

		JSONObject request = new JSONObject();
		request.put("query", new JSONObject(bqb.toString()));
		String query = request.toString();
		if (log.isDebugEnabled()) {
			log.debug(query);
		}
		return query;
	}

	protected String getSimpleWildCardQuery(String key, String value) throws JSONException {
		QueryBuilder qb = QueryBuilders.wildcardQuery(key, value);
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		bqb.must(qb);

		JSONObject request = new JSONObject();
		request.put("query", new JSONObject(bqb.toString()));
		String query = request.toString();
		if (log.isDebugEnabled()) {
			log.debug(query);
		}
		return query;
	}

	protected String getRangeQuery(String fieldName, double from, double to) throws JSONException {
		RangeQueryBuilder range = QueryBuilders.rangeQuery(fieldName).from(from).to(to);
		return "{ \"query\": " + range.toString() + "}";
	}

	/**
	 * Drop all indices and create a new index using the current data.
	 * 
	 * @throws Exception
	 */
	protected void recreateIndices() throws Exception {
		// We potentially modified existing data thus we need to drop all indices and create them and reindex all data
		searchProvider.clear();
		setupHandlers();
		IndexHandlerRegistry registry = MeshInternal.get().indexHandlerRegistry();
		for (IndexHandler handler : registry.getHandlers()) {
			handler.reindexAll().await();
		}
	}

}