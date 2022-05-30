package com.japhet.application.residentsincome.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.japhet.application.residentsincome.model.IncomeClass;
import com.japhet.application.residentsincome.model.IndividualIncome;

@Named
@Stateful
@ConversationScoped
public class IndividualIncomeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;
	
	@Inject
	private Logger LOG;

	@Resource
	private SessionContext sessionContext;

	@PersistenceContext(unitName = "residentsIncomePU", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	private Long id;
	private int page;
	private long count;
	private List<IndividualIncome> pageItems;
	private IndividualIncome incomeClass;
	private IndividualIncome defaultIncomeClass = new IndividualIncome();
	private IndividualIncome addIncomeClass = new IndividualIncome();

	public Long getId() {
		return id;
	}

	/*
	 * Supports updating and deleting IndividualIncome entities
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public IndividualIncome getIncomeClass() {
		return incomeClass;
	}

	/*
	 * Supports searching IndividualIncome with pagination
	 */
	public void setIncomeClass(IndividualIncome incomeClass) {
		this.incomeClass = incomeClass;
	}

	public String initializeConversationScope() { // create()
		this.conversation.begin();
		this.conversation.setTimeout(1800000L); // 30 minutes
		LOG.info("CONVERSATION SCOPED : " + conversation.toString());
		return "create?faces-redirect=true";
	}

	public void retrieveIndividualIncome() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.incomeClass = this.defaultIncomeClass;
		} else {
			this.incomeClass = findIncomeClassById(getId());
		}
	}

	public String updateIncomeClass() {
		this.conversation.end();
		try {
			if (this.id == null) {
				LOG.info("CREATE INCOME CLASS : " + incomeClass.toString());
				this.entityManager.persist(incomeClass);
				return "search?faces-redirect=true";
			} else {
				LOG.info("UPDATE INCOME CLASS : " + incomeClass.toString());
				this.entityManager.merge(incomeClass);
				return "view?faces-redirect=" + this.incomeClass.getId();
			}
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(ex.getMessage()));
			return null;
		}
	}

	public String deleteIncomeClass() {
		this.conversation.end();
		try {
			IndividualIncome deletableEntity = findIncomeClassById(
					getId());
			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(ex.getMessage()));
			return null;
		}
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public IndividualIncome getDefaultIncomeClass() {
		return defaultIncomeClass;
	}

	public void setDefaultIncomeClass(
			IndividualIncome defaultIncomeClass) {
		this.defaultIncomeClass = defaultIncomeClass;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// populate this.count

		CriteriaQuery<Long> countCriteria = builder
				.createQuery(Long.class);
		Root<IndividualIncome> root = countCriteria
				.from(IndividualIncome.class);
		countCriteria = countCriteria.select(builder.count(root))
				.where(getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// populate this.pageItems

		CriteriaQuery<IndividualIncome> incomeClassCriteria = builder
				.createQuery(IndividualIncome.class);
		root = incomeClassCriteria.from(IndividualIncome.class);
		TypedQuery<IndividualIncome> query = this.entityManager
				.createQuery(incomeClassCriteria.select(root)
						.where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize())
				.setMaxResults(getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<IndividualIncome> root) {
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<>();

		IncomeClass category = this.defaultIncomeClass.getCategory();
		if (category != null) {
			predicates.add(builder.equal(root.get("category"), category));
		}

		String description = this.defaultIncomeClass.getDescription();
		if (category != null && !"".equals(description)) {
			predicates.add(builder.like(
					builder.lower(root.<String>get("description")),
					'%' + description.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	/*
	 * Support listing and POSTing back Book entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */
	public long getCount() {
		return count;
	}

	public List<IndividualIncome> getPageItems() {
		return pageItems;
	}

	public List<IndividualIncome> listAll() {
		CriteriaQuery<IndividualIncome> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(IndividualIncome.class);
		return this.entityManager
				.createQuery(criteria
						.select(criteria.from(IndividualIncome.class)))
				.getResultList();
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
	public Converter<IndividualIncome> getConverter() {
		final IndividualIncomeBean ejbProxy = this.sessionContext
				.getBusinessObject(IndividualIncomeBean.class);

		return new Converter<IndividualIncome>() {
			public IndividualIncome getAsObject(FacesContext context,
					UIComponent component, String value) {
				return ejbProxy.findIncomeClassById(Long.valueOf(value));
			};

			public String getAsString(FacesContext context,
					UIComponent component, IndividualIncome value) {
				if (value == null) {
					return "";
				}

				return String.valueOf(value.getId());
			}
		};
	}

	public IndividualIncome findIncomeClassById(Long incomeId) {
		return this.entityManager.find(IndividualIncome.class, incomeId);
	}

	public IndividualIncome getAddIncomeClass() {
		return addIncomeClass;
	}

	public IndividualIncome getAddedIncomeClass() {
		IndividualIncome addedIncomeClass = this.addIncomeClass;
		this.addIncomeClass = new IndividualIncome();
		return addedIncomeClass;
	}
}
