/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  namespace Common {
    /** common params of paginating */
    interface PaginatingCommonParams {
      /** current page number */
      current: number;
      /** page size */
      size: number;
      /** total count */
      total: number;
    }

    /** common params of paginating query list data */
    interface PaginatingQueryRecord<T = any> extends PaginatingCommonParams {
      records: T[];
    }

    /** common search params of table */
    type CommonSearchParams = Pick<Common.PaginatingCommonParams, 'current' | 'size'>;

    /**
     * enable status
     *
     * - 0: disabled
     * - 1: enabled
     */
    type EnableStatus = 0 | 1;

    /** common record */
    type CommonRecord<T = any> = {
      /** record id */
      id: number;
      /** record creator */
      createBy: string;
      /** record create time */
      createTime: string;
      /** record updater */
      updateBy: string;
      /** record update time */
      updateTime: string;
      /** record status */
      status: EnableStatus;
    } & T;
  }

  /**
   * Namespace Auth
   *
   * Backend api module: "auth"
   */
  namespace Auth {
    interface LoginToken {
      token: string;
      refreshToken: string;
    }

    interface UserInfo {
      userId: string;
      username: string;
      nickname: string;
      avatar: string;
      roles: string[];
      permissions: string[];
      routes?: Route.MenuRoute[];
    }

    interface CaptchaData {
      uuid: string;
      img: string;
    }
  }

  /**
   * Namespace Route
   *
   * Backend api module: "route"
   */
  namespace Route {
    type ElegantConstRoute = import('@elegant-router/types').ElegantConstRoute;

    interface MenuRoute extends ElegantConstRoute {
      id: string;
    }

    interface UserRoute {
      routes: MenuRoute[];
      home: import('@elegant-router/types').LastLevelRouteKey;
    }
  }

  /**
   * namespace SystemManage
   *
   * backend api module: "systemManage"
   */
  namespace SystemManage {
    /** role */
    type Role = Common.CommonRecord<{
      /** role name */
      roleName: string;
      /** role code */
      roleCode: string;
      /** role key */
      roleKey: string;
      /** role description */
      description: string;
      /** role sort */
      roleSort: number;
      /** data scope */
      dataScope: string;
      /** menu check strictly */
      menuCheckStrictly: boolean;
      /** dept check strictly */
      deptCheckStrictly: boolean;
    }>;

    /** role search params */
    type RoleSearchParams = Partial<
      Pick<Api.SystemManage.Role, 'roleName' | 'roleCode' | 'roleKey' | 'status'> & Common.CommonSearchParams
    >;

    /** role list */
    type RoleList = Common.PaginatingQueryRecord<Role>;

    /** all role */
    type AllRole = Pick<Role, 'id' | 'roleName' | 'roleCode'>;

    /**
     * user gender
     *
     * - 0: male
     * - 1: female
     * - 2: unknown
     */
    type UserGender = 0 | 1 | 2;

    /** user */
    type User = Common.CommonRecord<{
      /** user name */
      username: string;
      /** nick name */
      nickname: string;
      /** user gender */
      gender: UserGender;
      /** user phone */
      phone: string;
      /** user email */
      email: string;
      /** avatar */
      avatar: string;
      /** dept ids */
      deptIds: number[];
      /** dept name */
      deptName: string;
      /** user role ids */
      roleIds: number[];
      /** user post ids */
      postIds: number[];
      /** remark */
      remark: string;
    }>;

    /** user search params */
    type UserSearchParams = Partial<
      Pick<Api.SystemManage.User, 'username' | 'gender' | 'nickname' | 'phone' | 'email' | 'status'> &
        Common.CommonSearchParams
    >;

    /** user list */
    type UserList = Common.PaginatingQueryRecord<User>;

    /**
     * menu type
     *
     * - "M": directory
     * - "C": menu
     * - "F": button
     */
    type MenuType = 'M' | 'C' | 'F';

    type MenuButton = {
      /**
       * button code
       *
       * it can be used to control the button permission
       */
      code: string;
      /** button description */
      desc: string;
    };

    /**
     * icon type
     *
     * - "1": iconify icon
     * - "2": local icon
     */
    type IconType = '1' | '2';

    type MenuPropsOfRoute = Pick<
      import('vue-router').RouteMeta,
      | 'i18nKey'
      | 'keepAlive'
      | 'constant'
      | 'order'
      | 'href'
      | 'hideInMenu'
      | 'activeMenu'
      | 'multiTab'
      | 'fixedIndexInTab'
      | 'query'
    >;

    type Menu = Common.CommonRecord<{
      /** parent menu id */
      parentId: number;
      /** menu type */
      menuType: MenuType;
      /** menu name */
      menuName: string;
      /** menu code */
      menuCode: string;
      /** route name */
      routeName: string;
      /** route path */
      routePath: string;
      /** component */
      component: string;
      /** permission */
      perms: string;
      /** visible */
      visible: string;
      /** is frame */
      isFrame: string;
      /** is cache */
      isCache: string;
      /** menu sort */
      menuSort: number;
      /** iconify icon name or local icon name */
      icon: string;
      /** icon type */
      iconType: IconType;
      /** buttons */
      buttons?: MenuButton[] | null;
      /** children menu */
      children?: Menu[];
    }> &
      MenuPropsOfRoute;

    /** menu list */
    type MenuList = Common.PaginatingQueryRecord<Menu>;

    type MenuTree = {
      id: number;
      label: string;
      pId: number;
      children?: MenuTree[];
    };

    /** dept */
    type Dept = Common.CommonRecord<{
      /** parent dept id */
      parentId: number;
      /** dept name */
      deptName: string;
      /** dept code */
      deptCode: string;
      /** order num */
      orderNum: number;
      /** leader */
      leader: string;
      /** phone */
      phone: string;
      /** email */
      email: string;
      /** children dept */
      children?: Dept[];
    }>;

    /** dept tree */
    type DeptTree = {
      id: number;
      label: string;
      pId: number;
      children?: DeptTree[];
    };

    /** post */
    type Post = Common.CommonRecord<{
      /** post name */
      postName: string;
      /** post code */
      postCode: string;
      /** post sort */
      postSort: number;
      /** remark */
      remark: string;
    }>;

    /** post search params */
    type PostSearchParams = Partial<
      Pick<Api.SystemManage.Post, 'postName' | 'postCode' | 'status'> & Common.CommonSearchParams
    >;

    /** post list */
    type PostList = Common.PaginatingQueryRecord<Post>;

    /** dict type */
    type DictType = Common.CommonRecord<{
      /** dict name */
      dictName: string;
      /** dict type */
      dictType: string;
      /** remark */
      remark: string;
    }>;

    /** dict type search params */
    type DictTypeSearchParams = Partial<
      Pick<Api.SystemManage.DictType, 'dictName' | 'dictType' | 'status'> & Common.CommonSearchParams
    >;

    /** dict type list */
    type DictTypeList = Common.PaginatingQueryRecord<DictType>;

    /** dict data */
    type DictData = Common.CommonRecord<{
      /** dict sort */
      dictSort: number;
      /** dict label */
      dictLabel: string;
      /** dict value */
      dictValue: string;
      /** dict type */
      dictType: string;
      /** css class */
      cssClass: string;
      /** list class */
      listClass: string;
      /** is default */
      isDefault: string;
      /** remark */
      remark: string;
    }>;

    /** dict data search params */
    type DictDataSearchParams = Partial<
      Pick<Api.SystemManage.DictData, 'dictLabel' | 'dictValue' | 'dictType' | 'status'> & Common.CommonSearchParams
    >;

    /** dict data list */
    type DictDataList = Common.PaginatingQueryRecord<DictData>;

    /** config */
    type Config = Common.CommonRecord<{
      /** config name */
      configName: string;
      /** config key */
      configKey: string;
      /** config value */
      configValue: string;
      /** config type */
      configType: string;
      /** remark */
      remark: string;
    }>;

    /** config search params */
    type ConfigSearchParams = Partial<
      Pick<Api.SystemManage.Config, 'configName' | 'configKey' | 'configType' | 'status'> & Common.CommonSearchParams
    >;

    /** config list */
    type ConfigList = Common.PaginatingQueryRecord<Config>;

    /** operation log */
    type OperLog = Common.CommonRecord<{
      /** title */
      title: string;
      /** business type */
      businessType: number;
      /** method */
      method: string;
      /** request method */
      requestMethod: string;
      /** operator type */
      operatorType: number;
      /** oper name */
      operName: string;
      /** dept name */
      deptName: string;
      /** oper url */
      operUrl: string;
      /** oper ip */
      operIp: string;
      /** oper location */
      operLocation: string;
      /** oper param */
      operParam: string;
      /** json result */
      jsonResult: string;
      /** error msg */
      errorMsg: string;
      /** oper time */
      operTime: string;
      /** cost time */
      costTime: number;
    }>;

    /** login info */
    type LoginInfo = Common.CommonRecord<{
      /** user name */
      username: string;
      /** ipaddr */
      ipaddr: string;
      /** login location */
      loginLocation: string;
      /** browser */
      browser: string;
      /** os */
      os: string;
      /** status */
      status: Common.EnableStatus;
      /** msg */
      msg: string;
      /** login time */
      loginTime: string;
    }>;
  }

  /**
   * namespace CacheManage
   *
   * backend api module: "cacheManage"
   */
  namespace CacheManage {
    /** Redis信息 */
    type RedisInfo = {
      redis_version: string;
      redis_mode: string;
      tcp_port: string;
      uptime_in_seconds: string;
      uptime_in_days: string;
      connected_clients: string;
      used_memory_human: string;
      used_memory_peak_human: string;
      maxmemory_human: string;
      mem_fragmentation_ratio: string;
      keyspace_hits: string;
      keyspace_misses: string;
      total_commands_processed: string;
      instantaneous_ops_per_sec: string;
      hit_rate: string;
    };

    /** JVM信息 */
    type JvmInfo = {
      heap_used: string;
      heap_committed: string;
      heap_max: string;
      non_heap_used: string;
      non_heap_committed: string;
      heap_usage: string;
    };

    /** 缓存统计信息 */
    type CacheInfo = {
      total_keys: number;
      cache_spaces: Record<string, number>;
    };

    /** 服务器信息 */
    type ServerInfo = {
      server_time: string;
      java_version: string;
      os_name: string;
      os_arch: string;
    };

    /** 缓存监控信息 */
    type MonitorInfo = {
      redis: RedisInfo;
      jvm: JvmInfo;
      cache: CacheInfo;
      server: ServerInfo;
    };

    /** 缓存项 */
    type CacheItem = {
      key: string;
      ttl: number;
      ttl_text: string;
      type: string;
      size: string;
      cache_space: string;
      cache_space_name: string;
    };

    /** 缓存详情 */
    type CacheDetail = CacheItem & {
      value: any;
    };

    /** 缓存列表响应 */
    type CacheListResponse = {
      records: CacheItem[];
      total: number;
      current: number;
      size: number;
      pages: number;
    };

    /** 缓存空间统计 */
    type CacheSpaceStat = {
      space: string;
      name: string;
      count: number;
      color: string;
    };

    /** 缓存统计 */
    type CacheStats = {
      spaces: CacheSpaceStat[];
      total: number;
    };
  }

  /** 文件管理模块 */
  namespace FileManage {
    /** 文件信息 */
    interface FileInfo {
      id: number;
      fileName: string;
      originalName: string;
      fileSuffix: string;
      url: string;
      size: number;
      configKey: string;
      filePath: string;
      contentType: string;
      uploadId?: string;
      uploadStatus: number;
      remark?: string;
      createTime: string;
      updateTime: string;
      createBy?: number;
      updateBy?: number;
    }

    /** 文件上传结果 */
    interface FileUploadResult {
      id: number;
      fileName: string;
      originalName: string;
      fileSuffix: string;
      url: string;
      size: number;
      configKey: string;
      filePath: string;
      contentType: string;
      uploadStatus: number;
    }

    /** 文件搜索参数 */
    interface FileSearchParams {
      current: number;
      size: number;
      fileName?: string;
      configKey?: string;
      contentType?: string;
    }

    /** 文件列表 */
    interface FileList {
      current: number;
      size: number;
      total: number;
      records: FileInfo[];
    }

    /** OSS配置信息 */
    interface OssConfig {
      id: number;
      configKey: string;
      accessKey?: string;
      secretKey?: string;
      bucketName?: string;
      prefix?: string;
      endpoint?: string;
      domain?: string;
      isHttps?: number;
      region?: string;
      accessPolicy?: number;
      status: number;
      ext1?: string;
      remark?: string;
      createTime: string;
      updateTime: string;
      createBy?: number;
      updateBy?: number;
    }

    /** OSS配置搜索参数 */
    interface OssConfigSearchParams {
      current: number;
      size: number;
      configKey?: string;
      status?: number;
    }

    /** OSS配置列表 */
    interface OssConfigList {
      current: number;
      size: number;
      total: number;
      records: OssConfig[];
    }
  }

  /** 日志管理 */
  namespace LogManage {
    /** 操作日志 */
    interface OperLog {
      /** 日志主键 */
      id?: number;
      /** 模块标题 */
      title?: string;
      /** 业务类型：0-其它，1-新增，2-修改，3-删除 */
      businessType?: number;
      /** 方法名称 */
      method?: string;
      /** 请求方式 */
      requestMethod?: string;
      /** 操作类别：0-其它，1-后台用户，2-手机端用户 */
      operatorType?: number;
      /** 操作人员 */
      operName?: string;
      /** 部门名称 */
      deptName?: string;
      /** 请求URL */
      operUrl?: string;
      /** 主机地址 */
      operIp?: string;
      /** 操作地点 */
      operLocation?: string;
      /** 请求参数 */
      operParam?: string;
      /** 返回参数 */
      jsonResult?: string;
      /** 操作状态：0-正常，1-异常 */
      status?: number;
      /** 错误消息 */
      errorMsg?: string;
      /** 操作时间 */
      operTime?: string;
      /** 消耗时间 */
      costTime?: number;
    }

    /** 操作日志搜索参数 */
    interface OperLogSearchParams {
      /** 当前页码 */
      current?: number;
      /** 页面大小 */
      size?: number;
      /** 模块标题 */
      title?: string;
      /** 操作人员 */
      operName?: string;
      /** 业务类型 */
      businessType?: number;
      /** 状态 */
      status?: number;
      /** 操作IP */
      operIp?: string;
    }

    /** 操作日志列表 */
    interface OperLogList {
      current: number;
      size: number;
      total: number;
      records: OperLog[];
    }

    /** 登录日志 */
    interface LoginLog {
      /** 访问ID */
      id?: number;
      /** 用户账号 */
      userName?: string;
      /** 登录IP地址 */
      ipaddr?: string;
      /** 登录地点 */
      loginLocation?: string;
      /** 浏览器类型 */
      browser?: string;
      /** 操作系统 */
      os?: string;
      /** 登录状态：0-成功，1-失败 */
      status?: string;
      /** 提示消息 */
      msg?: string;
      /** 访问时间 */
      loginTime?: string;
    }

    /** 登录日志搜索参数 */
    interface LoginLogSearchParams {
      /** 当前页码 */
      current?: number;
      /** 页面大小 */
      size?: number;
      /** 用户账号 */
      userName?: string;
      /** 登录IP地址 */
      ipaddr?: string;
      /** 登录状态 */
      status?: string;
      /** 登录地点 */
      loginLocation?: string;
    }

    /** 登录日志列表 */
    interface LoginLogList {
      current: number;
      size: number;
      total: number;
      records: LoginLog[];
    }
  }
}
