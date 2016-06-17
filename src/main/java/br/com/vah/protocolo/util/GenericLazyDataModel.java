package br.com.vah.protocolo.util;

import br.com.vah.protocolo.entities.BaseEntity;
import br.com.vah.protocolo.service.DataAccessService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Custom Lazy DataModel which extends PrimeFaces LazyDataModel. For more
 * information please visit
 * http://www.primefaces.org/showcase-labs/ui/datatableLazy.jsf
 */

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class GenericLazyDataModel<T extends BaseEntity> extends LazyDataModel<T> implements Serializable {

  // Data Source for binding data to the DataTable
  private List<T> datasource;
  // Selected Page size in the DataTable
  private int pageSize;
  // Current row index number
  private int rowIndex;
  // Total row number
  private int rowCount;
  // Data Access Service for create read update delete operations
  private DataAccessService crudService;
  /**
   * Search parms
   */
  private PaginatedSearchParam searchParams = new PaginatedSearchParam();

  /**
   * @param crudService
   */
  public GenericLazyDataModel(DataAccessService crudService) {
    this.crudService = crudService;
  }

  @Override
  public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

    if (searchParams.getResetPage()) {
      searchParams.setFirst(0);
      setRowIndex(0);
      searchParams.setResetPage(false);
    } else {
      searchParams.setFirst(first);
    }
    searchParams.setPageSize(pageSize);

    if (sortField != null && !sortField.isEmpty()) {
      searchParams.setOrderBy(sortField);
      searchParams.setAsc(SortOrder.ASCENDING.equals(sortOrder));
    }

    datasource = crudService.paginatedSearch(searchParams);

    setRowCount(crudService.paginatedCount(searchParams));

    return datasource;
  }

  public List<T> load(int limit) {
    searchParams.setFirst(0);
    searchParams.setPageSize(limit);
    return crudService.paginatedSearch(searchParams);
  }

  @Override
  public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
    return super.load(first, pageSize, multiSortMeta, filters);
  }

  public void remove(Integer index) {
    datasource.remove(index);
    setRowCount(getRowCount() - 1);
  }

  /**
   * Checks if the row is available
   *
   * @return boolean
   */
  @Override
  public boolean isRowAvailable() {
    if (datasource == null)
      return false;
    int index = rowIndex % pageSize;
    return index >= 0 && index < datasource.size();
  }

  /**
   * Gets the sector object's primary key
   *
   * @param sector
   * @return Object
   */
  @Override
  public Object getRowKey(T sector) {
    return sector.getId().toString();
  }

  /**
   * Returns the sector object at the specified position in datasource.
   *
   * @return
   */
  @Override
  public T getRowData() {
    if (datasource == null)
      return null;
    int index = rowIndex % pageSize;
    if (index > datasource.size()) {
      return null;
    }
    return datasource.get(index);
  }

  /**
   * Returns the sector object that has the row key.
   *
   * @param rowKey
   * @return
   */
  @Override
  public T getRowData(String rowKey) {
    if (datasource == null)
      return null;
    for (T item : datasource) {
      if (item.getId().toString().equals(rowKey))
        return item;
    }
    return null;
  }

	/*
   * ===== Getters and Setters of LazySectorDataModel fields
	 */

  /**
   * @param pageSize
   */
  @Override
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Returns page size
   *
   * @return int
   */
  @Override
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns current row index
   *
   * @return int
   */
  @Override
  public int getRowIndex() {
    return this.rowIndex;
  }

  /**
   * Sets row index
   *
   * @param rowIndex
   */
  @Override
  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  /**
   * Sets row count
   *
   * @param rowCount
   */
  @Override
  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  /**
   * Returns row count
   *
   * @return int
   */
  @Override
  public int getRowCount() {
    return this.rowCount;
  }

  /**
   * Sets wrapped data
   *
   * @param list
   */
  @Override
  public void setWrappedData(Object list) {
    this.datasource = (List<T>) list;
  }

  /**
   * Returns wrapped data
   *
   * @return
   */
  @Override
  public Object getWrappedData() {
    return datasource;
  }

  /**
   * @return the searchParams
   */
  public PaginatedSearchParam getSearchParams() {
    return searchParams;
  }
}
