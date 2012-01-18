/**
 * Copyright (C) 2009-2012 Antelink SAS
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License Version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version
 * 3 along with this program. If not, see http://www.gnu.org/licenses/agpl.html
 *
 * Additional permission under GNU AGPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with
 * Eclipse Java development tools (JDT) or Jetty (or a modified version of these
 * libraries), containing parts covered by the terms of Eclipse Public License 1.0,
 * the licensors of this Program grant you additional permission to convey the
 * resulting work. Corresponding Source for a non-source form of such a combination
 * shall include the source code for the parts of Eclipse Java development tools
 * (JDT) or Jetty used as well as that of the covered work.
 */
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
