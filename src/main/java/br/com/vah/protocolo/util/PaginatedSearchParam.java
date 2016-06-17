package br.com.vah.protocolo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginatedSearchParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> params;

	private Integer first;

	private Integer pageSize;

	private String orderBy;

	private Boolean asc;

	private Boolean resetPage = false;

	private List<String> relations = new ArrayList<>();

	public PaginatedSearchParam() {
		params = new HashMap<>();
		first = 0;
		pageSize = 0;
		asc = true;
	}

	/**
	 * 
	 * @param relations
	 */
	public void addRelations(String... relations) {
		if (relations != null) {
			for (String relation : relations) {
				this.relations.add(relation);
			}
		}
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @return the first
	 */
	public Integer getFirst() {
		return first;
	}

	/**
	 * @param first
	 *            the first to set
	 */
	public void setFirst(Integer first) {
		this.first = first;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the total to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the asc
	 */
	public Boolean getAsc() {
		return asc;
	}

	/**
	 * @param asc
	 *            the asc to set
	 */
	public void setAsc(Boolean asc) {
		this.asc = asc;
	}

	/**
	 * @return the resetPage
	 */
	public Boolean getResetPage() {
		return resetPage;
	}

	/**
	 * @param resetPage
	 *            the resetPage to set
	 */
	public void setResetPage(Boolean resetPage) {
		this.resetPage = resetPage;
	}

	/**
	 * @return the relations
	 */
	public List<String> getRelations() {
		return relations;
	}

	/**
	 * @param relations
	 *            the relations to set
	 */
	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

}
