/**
 * Copyright (c) 2010, Daniel Bimschas and Dennis Pfisterer, Institute of Telematics, University of Luebeck
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.itm.netty.handlerstack.dlestxetx;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DleStxEtxFramingDecoderTest {

	@Test
	public void testPacket() {

		final String payloadString = "hello, world";
		byte[] payload = payloadString.getBytes();

		ChannelBuffer encodedBuffer = ChannelBuffers.wrappedBuffer(
				DleStxEtxConstants.DLE_STX,
				payload,
				DleStxEtxConstants.DLE_ETX
		);

		testDecoding(encodedBuffer, payload);
	}

	@Test
	public void testRandomBytesBeforePacket() {

		final String payloadString = "hello, world";
		byte[] expectedPayload = payloadString.getBytes();

		ChannelBuffer encodedBuffer = ChannelBuffers.wrappedBuffer(
				"klasjd".getBytes(),
				DleStxEtxConstants.DLE_STX,
				expectedPayload,
				DleStxEtxConstants.DLE_ETX
		);

		testDecoding(encodedBuffer, expectedPayload);
	}

	@Test
	public void testRandomBytesAfterPacket() {

		final String payloadString = "hello, world";
		byte[] expectedPayload = payloadString.getBytes();

		ChannelBuffer encodedBuffer = ChannelBuffers.wrappedBuffer(
				DleStxEtxConstants.DLE_STX,
				expectedPayload,
				DleStxEtxConstants.DLE_ETX,
				"klasjd".getBytes()
		);

		testDecoding(encodedBuffer, expectedPayload);
	}

	@Test
	public void testRandomBytesBeforeAndAfterPacket() {

		final String payloadString = "hello, world";
		byte[] expectedPayload = payloadString.getBytes();

		ChannelBuffer encodedBuffer = ChannelBuffers.wrappedBuffer(
				"klasjd".getBytes(),
				DleStxEtxConstants.DLE_STX,
				expectedPayload,
				DleStxEtxConstants.DLE_ETX,
				"klasjd".getBytes()
		);

		testDecoding(encodedBuffer, expectedPayload);
	}

	private void testDecoding(ChannelBuffer encodedBuffer, byte[] expectedPayload) {
		DecoderEmbedder<ChannelBuffer> decoder = new DecoderEmbedder<ChannelBuffer>(new DleStxEtxFramingDecoder());
		decoder.offer(encodedBuffer);
		ChannelBuffer decodedBuffer = decoder.poll();
		byte[] decodedPayload = new byte[decodedBuffer.readableBytes()];
		decodedBuffer.readBytes(decodedPayload);
		assertArrayEquals(expectedPayload, decodedPayload);
	}

}
