import {computed, effectScope, onScopeDispose, reactive, ref, shallowRef, toValue, watch} from 'vue';
import type {MaybeRef, Ref} from 'vue';
import type {TablePaginationConfig} from 'ant-design-vue';
import type {TableRowSelection} from 'ant-design-vue/es/table/interface';
import {useElementSize} from '@vueuse/core';
import {useBoolean, useHookTable} from '@sa/hooks';
import {jsonClone} from '@sa/utils';
import {useAppStore} from '@/store/modules/app';
import {$t} from '@/locales';

type TableData = AntDesign.TableData;
type GetTableData<A extends AntDesign.TableApiFn> = AntDesign.GetTableData<A>;
type TableColumn<T> = AntDesign.TableColumn<T>;

export function useTable<A extends AntDesign.TableApiFn>(config: AntDesign.AntDesignTableConfig<A>) {
  const scope = effectScope();
  const appStore = useAppStore();

  const {apiFn, apiParams, immediate} = config;

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
      // 如果返回的是数组，直接使用
      if (Array.isArray(res)) {
        return {
          data: res,
          pageNum: 1,
          pageSize: 10,
          total: res.length
        };
      }

      // 数据提取，兼容不同的后端响应格式
      let records: any[] = [];
      let current = 1;
      let size = 10;
      let total = 0;

      // 使用any类型断言，避免TypeScript错误
      const anyRes = res as any;

      if (anyRes && typeof anyRes === 'object') {
        // 直接从响应中获取records字段
        if (Array.isArray(anyRes.records)) {
          records = anyRes.records;
          current = anyRes.current || anyRes.pageNumber || 1;
          size = anyRes.size || anyRes.pageSize || 10;
          total = anyRes.total || anyRes.totalRow || 0;
        }
        // 从res.data中获取records字段
        else if (anyRes.data && typeof anyRes.data === 'object') {
          if (Array.isArray(anyRes.data.records)) {
            records = anyRes.data.records;
            current = anyRes.data.current || anyRes.data.pageNumber || 1;
            size = anyRes.data.size || anyRes.data.pageSize || 10;
            total = anyRes.data.total || anyRes.data.totalRow || 0;
          }
          // res.data本身就是数组
          else if (Array.isArray(anyRes.data)) {
            records = anyRes.data;
          }
        }
      }

      // 确保pageSize大于0
      const pageSize = size <= 0 ? 10 : size;

      // 添加序号
      const recordsWithIndex = records.map((item, index) => {
        return {
          ...item,
          index: (current - 1) * pageSize + index + 1
        };
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
      const {pageNum, pageSize, total} = transformed;

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
    getData,
    searchParams,
    updateSearchParams,
    resetSearchParams,
    pagination,
    mobilePagination,
    updatePagination,
    getDataByPage
  };
}

export function useTableOperate<T extends TableData = TableData>(data: Ref<T[]>, getData: () => Promise<void>) {
  const {bool: drawerVisible, setTrue: openDrawer, setFalse: closeDrawer} = useBoolean();

  const operateType = ref<AntDesign.TableOperateType>('add');

  function handleAdd() {
    operateType.value = 'add';
    openDrawer();
  }

  /** the edit row data */
  const editingData: Ref<T | null> = ref(null);

  function handleEdit(id: T['id']) {
    const findItem = data.value.find(item => item.id === id) || null;
    editingData.value = jsonClone(findItem);

    operateType.value = 'edit';
    openDrawer();
  }

  /** the checked row keys of table */
  const checkedRowKeys = ref<string[]>([]);

  /** the hook of table selection */
  const rowSelection: TableRowSelection = reactive({
    type: 'checkbox',
    selectedRowKeys: checkedRowKeys,
    onChange: onSelectChange
  });

  function onSelectChange(keys: (string | number)[]) {
    checkedRowKeys.value = keys as string[];
  }

  // return true to close the drawer
  function handleSubmit() {
    closeDrawer();
  }

  function onDeleted() {
    closeDrawer();

    getData();
  }

  async function onBatchDeleted() {
    getData();

    checkedRowKeys.value = [];
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
    rowSelection,
    onSelectChange,
    handleSubmit,
    onDeleted,
    onBatchDeleted
  };
}

export function useTableScroll(scrollX: MaybeRef<number> = 702) {
  const el = shallowRef<HTMLElement>();

  const {width} = useElementSize(el);

  const scrollConfig = computed(() => {
    const scroll = toValue(scrollX);

    return {
      x: scroll
    };
  });

  return {
    el,
    scrollConfig
  };
}

/**
 * 简单的分页Hook
 * 用于兼容现有的文件管理页面
 */
export function usePagination(getData: () => void) {
  const pagination = reactive({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total: number, range: [number, number]) => `共 ${total} 条记录`
  });

  const mobilePagination = computed(() => {
    const appStore = useAppStore();
    return {
      ...pagination,
      simple: appStore.isMobile
    };
  });

  const searchParams = reactive({});

  function resetSearchParams() {
    Object.keys(searchParams).forEach(key => {
      (searchParams as any)[key] = '';
    });
    pagination.current = 1;
    getData();
  }

  return {
    pagination,
    mobilePagination,
    searchParams,
    resetSearchParams
  };
}
