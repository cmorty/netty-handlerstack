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
package de.uniluebeck.itm.netty.handlerstack.tinyos;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.tr.util.StringUtils;

public class HdlcTranslateEncoder extends OneToOneEncoder {

    private final Logger log;

    public HdlcTranslateEncoder() {
        this(null);
    }
    
    public HdlcTranslateEncoder(String instanceName) {
        log = LoggerFactory.getLogger(instanceName != null ? instanceName : HdlcTranslateEncoder.class.getName());
    }

    @Override
    protected Object encode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {

        if (!(msg instanceof ChannelBuffer)) {
            return msg;
        }

        ChannelBuffer buffer = (ChannelBuffer) msg;
        ChannelBuffer packet = ChannelBuffers.dynamicBuffer(buffer.readableBytes() + 4);

		// start delimiter
		packet.writeByte(HdlcTranslateConstants.FRAME_DELIMITER_BYTE);

		for (int i = buffer.readerIndex(); i < (buffer.readerIndex() + buffer.readableBytes()); i++) {

			byte b = buffer.getByte(i);

			// escape bytes if needed
            if (b == HdlcTranslateConstants.ESCAPE_BYTE || b == HdlcTranslateConstants.FRAME_DELIMITER_BYTE) {
                packet.writeByte(HdlcTranslateConstants.ESCAPE_BYTE);
				packet.writeByte(b ^ 0x20);
			} else {
				packet.writeByte(b);
			}

        }

		// end delimiter
        packet.writeByte(HdlcTranslateConstants.FRAME_DELIMITER_BYTE);

        if (log.isTraceEnabled()) {
            log.trace("Encoded buffer: {}", StringUtils.toHexString(packet.toByteBuffer().array()));
        }

        return packet;
    }
}
