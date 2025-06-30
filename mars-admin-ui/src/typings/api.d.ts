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
}
