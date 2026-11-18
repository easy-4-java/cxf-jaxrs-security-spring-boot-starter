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
package org.apache.cxf.spring.boot.jaxrs;

import java.util.Calendar;

import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.MatrixParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.DefaultMethod;

@Path(value = "/customer")
// @Produces("*/*")
//@Produces("application/xml")
// @Produces("application/json")
public class CustomerServiceImpl4 {

	@POST
	@Path("{id}")
	@Consumes
	public void post(@BeanParam Customer myBean) {

	}

	@GET
	@Path("/{id}/info")
	@Consumes
	@Produces("application/xml")
	public Customer findCustomerById(@PathParam("id") @DefaultValue("ddd") String id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setName("xiaojing" + id);
		customer.setBirthday(Calendar.getInstance().getTime());
		System.out.println(">>>>>>>>>>>>>服务端信息：" + customer);
		return customer;
	}

	@GET
	@Path(value = "/search")
	public Customer findCustomerByName(@QueryParam("name") String name) {
		Customer customer = new Customer();
		customer.setId(name);
		customer.setName(name);
		customer.setBirthday(Calendar.getInstance().getTime());
		System.out.println(">>>>>>>>>>>>>服务端信息：" + customer);
		return customer;
	}
}