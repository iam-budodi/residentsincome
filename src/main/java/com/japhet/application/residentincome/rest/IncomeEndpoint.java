package com.japhet.application.residentincome.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.List;

import com.japhet.application.residentincome.model.IndividualIncome;
import com.japhet.application.residentincome.service.IncomeService;

import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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
	public Response deleteIncomeClass(@PathParam("id") @NotNull @Min(1) Long incomeClassId) {
		IndividualIncome incomeClass = findIncomeClass(incomeClassId);
		if (incomeClass == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		incomeService.deleteIncomeClass(incomeClassId);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@PUT
	@Path("{id: \\d+}")
	@Consumes(APPLICATION_JSON)
	public Response updateIncomeClass(@PathParam("id") @NotNull @Min(1) Long incomeClassId,
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
