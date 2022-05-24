package com.japhet.application.residentsincome.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.List;

import com.japhet.application.residentsincome.model.IndividualIncome;
import com.japhet.application.residentsincome.service.IncomeService;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/incomes")
public class IncomeEndpoint {

	@Inject
	private IncomeService incomeService;

	@GET
	@Produces(APPLICATION_JSON)
	public Response listIncomes() {
		List<IndividualIncome> incomeClasses = incomeService.listAllIncomes();

		if (incomeClasses.isEmpty())
			return Response.status(Response.Status.NO_CONTENT).build();

		return Response.ok(incomeClasses).build();
	}

	@GET
	@Path("{id: \\d+}")
	@Produces(APPLICATION_JSON)
	public Response findIncomeById(
				@PathParam("id") @Min(1) @NotNull Long incomeClassId) {

		IndividualIncome incomeClass = findIncomeClass(incomeClassId);
		if (incomeClass == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(incomeClass).build();
	}

	@POST
	@Consumes(APPLICATION_JSON)
	public Response createIncomeClass(@NotNull IndividualIncome incomeClass,
				@Context() UriInfo uriInfo) {
		try {
			incomeClass = incomeService.createIncomeClass(incomeClass);
		} catch (Exception rollbackException) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String incomeClassId = String.valueOf(incomeClass.getId());
		URI createdUri = uriInfo.getAbsolutePathBuilder().path(incomeClassId)
					.build();
		return Response.created(createdUri).build();
	}

	@DELETE
	@Path("{id: \\d+}")
	public Response deleteIncomeClass(
				@PathParam("id") @NotNull @Min(1) Long incomeClassId) {
		IndividualIncome incomeClass = findIncomeClass(incomeClassId);
		if (incomeClass == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		incomeService.deleteIncomeClass(incomeClassId);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@PUT
	@Path("{id: \\d+}")
	@Consumes(APPLICATION_JSON)
	public Response updateIncomeClass(
				@PathParam("id") @NotNull @Min(1) Long incomeClassId,
				@NotNull IndividualIncome incomeClass) {
		if (!incomeClassId.equals(incomeClass.getId())) {
			return Response.status(Response.Status.CONFLICT).build();
		}

		if (findIncomeClass(incomeClassId) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		try {
			incomeService.updateIncomeClass(incomeClass);
		} catch (OptimisticLockException ex) {
			return Response.status(Response.Status.CONFLICT)
						.entity(ex.getEntity()).build();
		}

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// ===========================================
	// Define a single common method
	// ==========================================

	public IndividualIncome findIncomeClass(Long incomeClassId) {
		return incomeService.findIncomeById(incomeClassId);

	}
}
