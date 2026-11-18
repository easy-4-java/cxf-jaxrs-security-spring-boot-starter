/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.cxf.spring.boot.jaxrs.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;

public class CustomResponseFilter implements ContainerResponseFilter {
	
	public void filter(ContainerRequestContext inContext, ContainerResponseContext outContext) {
		
		Message message = JAXRSUtils.getCurrentMessage();
		OperationResourceInfo cri = message.getExchange().get(OperationResourceInfo.class);

		// or consider using JAX-RS 2.0 ResourceInfo context

		// finally, work with ContainerResponseContext to modify specific Response
		// properties
	}
	
}
