import { computed, effectScope, onScopeDispose, reactive, ref, shallowRef, toValue, watch } from 'vue';
import type { MaybeRef, Ref } from 'vue';
import type { TablePaginationConfig } from 'ant-design-vue';
import type { TableRowSelection } from 'ant-design-vue/es/table/interface';
import { useElementSize } from '@vueuse/core';
import { useBoolean, useHookTable } from '@sa/hooks';
import { jsonClone } from '@sa/utils';
import { useAppStore } from '@/store/modules/app';
import { $t } from '@/locales';

type TableData = AntDesign.TableData;
type GetTableData<A extends AntDesign.TableApiFn> = AntDesign.GetTableData<A>;
type TableColumn<T> = AntDesign.TableColumn<T>;

export function useTable<A extends AntDesign.TableApiFn>(config: AntDesign.AntDesignTableConfig<A>) {
  const scope = effectScope();
  const appStore = useAppStore();

  const { apiFn, apiParams, immediate } = config;

  const {
    loading,
    empty,
    data,
    columns,
    columnChecks,
    reloadColumns,
    getData,
    searchParams,
    updateSearchParams,
    resetSearchParams
  } = useHookTable<A, GetTableData<A>, TableColumn<AntDesign.TableDataWithIndex<GetTableData<A>>>>({
    apiFn,
    apiParams,
    columns: config.columns,
    transformer: res => {
      console.log('useTable transformer 收到的响应数据:', res);
      
      // 处理null或undefined情况
      if (!res) {
        console.warn('transformer接收到空数据');
        return { data: [], pageNum: 1, pageSize: 10, total: 0 };
      }
      
      // 首先处理响应的标准结构，提取data字段
      // res格式: { code: 200, message: "操作成功", data: {...} }
      const responseData = res.data;
      
      // 如果原始响应中没有data字段，则尝试直接使用响应
      const dataToProcess = responseData !== undefined ? responseData : res;
      
      console.log('提取的数据部分:', dataToProcess);
      
      // 处理直接返回数组的情况（如菜单列表）
      if (Array.isArray(dataToProcess)) {
        console.log('处理数组类型响应');
        return {
          data: dataToProcess.map((item, index) => ({ ...item, index: index + 1 })),
          pageNum: 1,
          pageSize: dataToProcess.length,
          total: dataToProcess.length
        };
      }
      
      // 处理分页数据（检查多种可能的字段名）
      // 日志输出详细信息，帮助调试
      console.log('处理分页数据对象，结构:', {
        hasRecords: Boolean(dataToProcess.records),
        hasCurrent: Boolean(dataToProcess.pageNumber || dataToProcess.current),
        hasSize: Boolean(dataToProcess.pageSize || dataToProcess.size),
        hasTotal: Boolean(dataToProcess.totalRow || dataToProcess.total)
      });
      
      // 处理后端响应中的不同字段命名
      const records = dataToProcess.records || [];
      const current = dataToProcess.pageNumber || dataToProcess.current || 1;
      const size = dataToProcess.pageSize || dataToProcess.size || 10;
      const total = dataToProcess.totalRow || dataToProcess.total || 0;
      
      console.log('transformer提取的字段:', { records, current, size, total });

      // 确保size大于0，如果小于0会导致分页计算错误
      const pageSize = size <= 0 ? 10 : size;
      
      // 如果records不是数组（可能是undefined或null），创建空数组
      const safeRecords = Array.isArray(records) ? records : [];

      const recordsWithIndex = safeRecords.map((item, index) => {
        return {
          ...item,
          index: (current - 1) * pageSize + index + 1
        };
      });
      
      console.log('transformer处理后返回数据:', {
        data: recordsWithIndex, 
        pageNum: current,
        pageSize,
        total
      });

      return {
        data: recordsWithIndex,
        pageNum: current,
        pageSize,
        total
      };
    },
    getColumnChecks: cols => {
      const checks: AntDesign.TableColumnCheck[] = [];

      cols.forEach(column => {
        if (column.key) {
          checks.push({
            key: column.key as string,
            title: column.title as string,
            checked: true
          });
        }
      });

      return checks;
    },
    getColumns: (cols, checks) => {
      const columnMap = new Map<string, TableColumn<GetTableData<A>>>();

      cols.forEach(column => {
        if (column.key) {
          columnMap.set(column.key as string, column);
        }
      });

      const filteredColumns = checks
        .filter(item => item.checked)
        .map(check => columnMap.get(check.key) as TableColumn<GetTableData<A>>);

      return filteredColumns;
    },
    onFetched: async transformed => {
      const { pageNum, pageSize, total } = transformed;

      updatePagination({
        current: pageNum,
        pageSize,
        total
      });
    },
    immediate
  });

  const pagination: TablePaginationConfig = reactive({
    current: 1,
    pageSize: 10,
    showSizeChanger: true,
    pageSizeOptions: ['10', '15', '20', '25', '30'],
    total: 0,
    onChange: async (current: number, size: number) => {
      pagination.current = current;

      updateSearchParams({
        current,
        size
      });

      getData();
    }
  });

  // this is for mobile, if the system does not support mobile, you can use `pagination` directly
  const mobilePagination = computed(() => {
    const p: TablePaginationConfig = {
      ...pagination,
      simple: appStore.isMobile
    };

    return p;
  });

  function updatePagination(update: Partial<TablePaginationConfig>) {
    Object.assign(pagination, update);
  }

  /**
   * get data by page number
   *
   * @param pageNum the page number. default is 1
   */
  async function getDataByPage(pageNum: number = 1) {
    updatePagination({
      current: pageNum
    });

    updateSearchParams({
      current: pageNum,
      size: pagination.pageSize!
    });

    await getData();
  }

  scope.run(() => {
    watch(
      () => appStore.locale,
      () => {
        reloadColumns();
      }
    );
  });

  onScopeDispose(() => {
    scope.stop();
  });

  return {
    loading,
    empty,
    data,
    columns,
    columnChecks,
    reloadColumns,
    pagination,
    mobilePagination,
    updatePagination,
    getData,
    getDataByPage,
    searchParams,
    updateSearchParams,
    resetSearchParams
  };
}

export function useTableOperate<T extends TableData = TableData>(data: Ref<T[]>, getData: () => Promise<void>) {
  const { bool: drawerVisible, setTrue: openDrawer, setFalse: closeDrawer } = useBoolean();

  const operateType = ref<AntDesign.TableOperateType>('add');

  function handleAdd() {
    operateType.value = 'add';
    openDrawer();
  }

  /** the editing row data */
  const editingData: Ref<T | null> = ref(null);

  function handleEdit(id: T['id']) {
    operateType.value = 'edit';
    const findItem = data.value.find(item => item.id === id) || null;
    editingData.value = jsonClone(findItem);

    openDrawer();
  }

  /** the checked row keys of table */
  const checkedRowKeys: Ref<T['id'][]> = ref([]);

  function onSelectChange(keys: (string | number)[]) {
    checkedRowKeys.value = keys as T['id'][];
  }

  const rowSelection = computed<TableRowSelection<T>>(() => {
    return {
      columnWidth: 48,
      type: 'checkbox',
      selectedRowKeys: checkedRowKeys.value,
      onChange: onSelectChange
    };
  });

  /** the hook after the batch delete operation is completed */
  async function onBatchDeleted() {
    window.$message?.success($t('common.deleteSuccess'));

    checkedRowKeys.value = [];

    await getData();
  }

  /** the hook after the delete operation is completed */
  async function onDeleted() {
    window.$message?.success($t('common.deleteSuccess'));

    await getData();
  }

  return {
    drawerVisible,
    openDrawer,
    closeDrawer,
    operateType,
    handleAdd,
    editingData,
    handleEdit,
    checkedRowKeys,
    onSelectChange,
    rowSelection,
    onBatchDeleted,
    onDeleted
  };
}

export function useTableScroll(scrollX: MaybeRef<number> = 702) {
  const tableWrapperRef = shallowRef<HTMLElement | null>(null);
  const { height: wrapperElHeight } = useElementSize(tableWrapperRef);

  const scrollConfig = computed(() => {
    return {
      y: wrapperElHeight.value - 72,
      x: toValue(scrollX)
    };
  });

  return {
    tableWrapperRef,
    scrollConfig
  };
}
