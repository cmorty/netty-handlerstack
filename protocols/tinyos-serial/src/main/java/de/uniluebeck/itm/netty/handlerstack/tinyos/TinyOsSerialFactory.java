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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.uniluebeck.itm.netty.handlerstack.HandlerFactory;
import de.uniluebeck.itm.tr.util.Tuple;
import org.jboss.netty.channel.ChannelHandler;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class TinyOsSerialFactory implements HandlerFactory {

	@Override
	public List<Tuple<String, ChannelHandler>> create(@Nullable final String instanceName,
													  final Multimap<String, String> properties) throws Exception {

		List<Tuple<String, ChannelHandler>> handlers = new LinkedList<Tuple<String, ChannelHandler>>();
		handlers.addAll(new TinyOsSerialDecoderFactory().create(instanceName + "-decoder", properties));
		handlers.addAll(new TinyOsSerialEncoderFactory().create(instanceName + "-encoder", properties));
		return handlers;
	}

	@Override
	public List<Tuple<String, ChannelHandler>> create(Multimap<String, String> properties) throws Exception {
		return create(null, properties);
	}

	@Override
	public Multimap<String, String> getConfigurationOptions() {
		return HashMultimap.create();
	}

	@Override
	public String getDescription() {
		return "Both tinyos-serial-encoder and tinyos-serial-decoder. For more details see http://www.tinyos.net/tinyos-2.x/doc/html/tep113.html";
	}

	@Override
	public String getName() {
		return "tinyos-serial";
	}
}
