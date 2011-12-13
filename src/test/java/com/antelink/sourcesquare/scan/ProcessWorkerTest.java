package com.antelink.sourcesquare.scan;

import java.io.IOException;
import java.util.HashMap;

import org.easymock.Capture;
import org.easymock.IAnswer;

import static org.easymock.EasyMock.*;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.antelink.sourcesquare.client.scan.ProcessWorker;
import com.antelink.sourcesquare.client.scan.SourceSquareEngine;

public class ProcessWorkerTest {
	SourceSquareEngine engine;
	Capture<HashMap<String, String>> captureData;
	ProcessWorker worker ;
	HashMap<String, String> inputMap=new HashMap<String, String>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	{
		put("first","ASDF");
		put("second","BSDF");
		put("third","CSDF");
		put("fourth","DSDF");
	}};
	
	@Before
	public void setup(){
		engine = EasyMock
				.createMock(SourceSquareEngine.class);
		worker = new ProcessWorker(0, engine, this);
		captureData = new Capture<HashMap<String, String>>();

	}
	@After
	public void cleanTest(){
		EasyMock.reset(engine);
	}

	@Test
	public void testWorker() {
		try {
			engine.discover(capture(captureData));
		} catch (Exception e) {
			fail();
		}
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
			
				assertEquals("ASDF", captureData.getValue().get("first"));
				assertEquals(inputMap, captureData.getValue());
				return null;
			}
			
		});
		
		replay(engine);
		worker.process(inputMap);
		try {
			worker.getExecutor().join();
		} catch (InterruptedException e) {}
		verify(engine);

	}
	@Test
	public void testRetryOK(){
		
		try {
			engine.discover(capture(captureData));
			expectLastCall().andThrow(new IOException("")).andThrow(new IOException("")).andThrow(new IOException("")).andAnswer(new IAnswer<Object>() {
				@Override
				public Object answer() throws Throwable {
					assertEquals(inputMap, captureData.getValue());
					return null;
				}
			});
	
		} catch (Exception e) {}

		replay(engine);
		worker.process(inputMap);
		try {
			worker.getExecutor().join();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}
		verify(engine);
	}
	@Test
	public void testRetryFail(){
		try {
			engine.discover(capture(captureData));
			expectLastCall().andThrow(new IOException("")).andThrow(new IOException("")).andThrow(new IOException("")).andThrow(new IOException("")).andThrow(new IOException(""));
	
		} catch (Exception e) {}

		replay(engine);
		worker.process(inputMap);
		try {
			assertFalse(worker.isAvailable());
			worker.getExecutor().join();
		} catch (InterruptedException e) {
			fail();
			e.printStackTrace();
		}
		verify(engine);
		assertTrue(worker.isAvailable());
	}
	
}
