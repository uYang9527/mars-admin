/**
 * 模拟数据
 */

// 用户信息
export const userInfo = {
  id: 10001,
  name: '张三',
  avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
  position: '高级前端工程师',
  department: '研发中心-前端组',
  phone: '13800138000',
  email: 'zhangsan@company.com',
  joinDate: '2022-01-01'
}

// 首页轮播图
export const bannerList = [
  {
    id: 1,
    image: 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
    title: '企业文化建设'
  },
  {
    id: 2,
    image: 'https://images.unsplash.com/photo-1577563908411-5077b6dc7624?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
    title: '年度团队拓展活动'
  },
  {
    id: 3,
    image: 'https://images.unsplash.com/photo-1560518883-ce09059eeffa?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
    title: '产品发布会'
  }
]

// 快捷功能菜单
export const quickMenuList = [
  {
    id: 1,
    name: '考勤打卡',
    icon: 'clock-fill',
    color: '#2979ff',
    badge: 0,
    action: 'attendance'
  },
  {
    id: 2,
    name: '请假申请',
    icon: 'calendar',
    color: '#fa3534',
    badge: 0,
    action: 'leave'
  },
  {
    id: 3,
    name: '报销申请',
    icon: 'rmb-circle',
    color: '#ff9900',
    badge: 0,
    action: 'expense'
  },
  {
    id: 4,
    name: '工作汇报',
    icon: 'file-text',
    color: '#19be6b',
    badge: 0,
    action: 'report'
  },
  {
    id: 5,
    name: '会议室预订',
    icon: 'chat',
    color: '#9013fe',
    badge: 0,
    action: 'meeting'
  },
  {
    id: 6,
    name: '通讯录',
    icon: 'man-add',
    color: '#1cbbb4',
    badge: 0,
    action: 'contacts'
  },
  {
    id: 7,
    name: '文件管理',
    icon: 'folder',
    color: '#2979ff',
    badge: 2,
    action: 'files'
  },
  {
    id: 8,
    name: '更多应用',
    icon: 'more-circle',
    color: '#909399',
    badge: 0,
    action: 'more'
  }
]

// 公告列表
export const noticeList = [
  {
    id: 1,
    title: '关于2024年春节放假安排的通知',
    content: '根据国家法定假期安排，2024年春节放假时间为2月10日至2月17日，共8天。2月18日(周日)正常上班。',
    time: '2024-01-15',
    type: '公告'
  },
  {
    id: 2,
    title: '公司年度表彰大会即将召开',
    content: '公司年度表彰大会将于1月25日14:00在多功能会议室举行，请各部门负责人组织员工准时参加。',
    time: '2024-01-10',
    type: '通知'
  },
  {
    id: 3,
    title: '新员工入职培训安排',
    content: '新一批员工入职培训将于下周一开始，培训内容包括公司制度、业务流程和系统使用等。',
    time: '2024-01-08',
    type: '培训'
  }
]

// 工作动态
export const workList = [
  {
    id: 1,
    name: '张三',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    description: '完成了产品需求分析报告',
    time: '2小时前'
  },
  {
    id: 2,
    name: '李四',
    avatar: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    description: '提交了项目进度汇报',
    time: '4小时前'
  },
  {
    id: 3,
    name: '王五',
    avatar: 'https://images.unsplash.com/photo-1580489944761-15a19d654956?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
    description: '完成了客户需求调研',
    time: '6小时前'
  }
]

// 统计数据
export const statsData = [
  {
    label: '员工总数',
    value: '1,234'
  },
  {
    label: '部门数量',
    value: '56'
  },
  {
    label: '出勤率',
    value: '89%'
  },
  {
    label: '项目数量',
    value: '23'
  }
]

// 部门列表
export const departmentList = [
  {
    id: 1,
    name: '总经理办公室',
    memberCount: 5,
    expanded: false,
    manager: '李总',
    location: '8楼801室',
    phone: '0755-88887777',
    description: '负责公司整体战略规划和运营管理',
    members: [
      {
        id: 101,
        name: '李总',
        position: '总经理',
        avatar: 'https://images.unsplash.com/photo-1550525811-e5869dd03032?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900001111',
        email: 'lizongjingli@company.com',
        department: '总经理办公室',
        joinDate: '2020-01-01'
      },
      {
        id: 102,
        name: '王秘书',
        position: '行政秘书',
        avatar: 'https://images.unsplash.com/photo-1546961329-78bef0414d7c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900002222',
        email: 'wangmishu@company.com',
        department: '总经理办公室',
        joinDate: '2020-02-01'
      }
    ]
  },
  {
    id: 2,
    name: '研发中心',
    memberCount: 120,
    expanded: false,
    manager: '张主管',
    location: '5楼整层',
    phone: '0755-88886666',
    description: '负责公司产品研发和技术创新',
    members: [
      {
        id: 201,
        name: '张主管',
        position: '研发总监',
        avatar: 'https://images.unsplash.com/photo-1517021897933-0e0319cfbc28?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900003333',
        email: 'zhangzhuguang@company.com',
        department: '研发中心',
        joinDate: '2020-03-01'
      },
      {
        id: 202,
        name: '刘工程师',
        position: '高级工程师',
        avatar: 'https://images.unsplash.com/photo-1568602471122-7832951cc4c5?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900004444',
        email: 'liugongchengshi@company.com',
        department: '研发中心',
        joinDate: '2020-04-01'
      }
    ],
    children: [
      {
        id: 21,
        name: '前端开发部',
        memberCount: 25,
        expanded: false,
        members: [
          {
            id: 211,
            name: '赵前端',
            position: '前端开发',
            avatar: 'https://images.unsplash.com/photo-1583195764036-6dc248ac07d1?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
            phone: '13900005555',
            email: 'zhaoqianduan@company.com',
            department: '研发中心-前端开发部',
            joinDate: '2021-01-15'
          }
        ]
      },
      {
        id: 22,
        name: '后端开发部',
        memberCount: 35,
        expanded: false,
        members: [
          {
            id: 221,
            name: '钱后端',
            position: '后端开发',
            avatar: 'https://images.unsplash.com/photo-1600486913747-55e5470d6f40?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
            phone: '13900006666',
            email: 'qianhouduan@company.com',
            department: '研发中心-后端开发部',
            joinDate: '2021-02-15'
          }
        ]
      }
    ]
  },
  {
    id: 3,
    name: '市场部',
    memberCount: 45,
    expanded: false,
    manager: '孙经理',
    location: '3楼301-310室',
    phone: '0755-88885555',
    description: '负责公司市场营销和品牌推广',
    members: [
      {
        id: 301,
        name: '孙经理',
        position: '市场总监',
        avatar: 'https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900007777',
        email: 'sunjingli@company.com',
        department: '市场部',
        joinDate: '2020-05-01'
      },
      {
        id: 302,
        name: '周专员',
        position: '市场专员',
        avatar: 'https://images.unsplash.com/photo-1581235720704-06d3acfcb36f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
        phone: '13900008888',
        email: 'zhouzhuanyuan@company.com',
        department: '市场部',
        joinDate: '2020-06-01'
      }
    ]
  }
]

// 消息列表
export const messageList = {
  system: [
    {
      id: 1,
      title: '系统维护通知',
      content: '系统将于今晚22:00-24:00进行维护升级，期间可能影响正常使用，请提前做好准备。',
      time: '2024-01-15 14:30',
      read: false
    },
    {
      id: 2,
      title: '密码安全提醒',
      content: '为了您的账户安全，建议定期更换密码，密码应包含字母、数字和特殊字符。',
      time: '2024-01-14 09:15',
      read: true
    },
    {
      id: 3,
      title: '功能更新说明',
      content: '新版本已发布，新增了考勤统计、报表导出等功能，快来体验吧！',
      time: '2024-01-13 16:45',
      read: false
    }
  ],
  work: [
    {
      id: 4,
      title: '项目进度汇报',
      content: '请于本周五前提交项目进度汇报，包括完成情况、遇到问题及下周计划。',
      time: '2024-01-15 10:20',
      read: false,
      priority: 'high',
      priorityText: '高优先级'
    },
    {
      id: 5,
      title: '会议室预订确认',
      content: '您预订的会议室A-101已确认，会议时间：明天下午2:00-4:00。',
      time: '2024-01-14 17:30',
      read: true,
      priority: 'normal',
      priorityText: '普通'
    },
    {
      id: 6,
      title: '任务指派通知',
      content: '您有一个新任务"优化登录界面"已指派给您，请尽快处理。',
      time: '2024-01-13 11:05',
      read: false,
      priority: 'medium',
      priorityText: '中优先级'
    }
  ],
  approval: [
    {
      id: 7,
      title: '请假申请审批',
      content: '您的请假申请（1月20日至1月22日）已审批通过。',
      time: '2024-01-15 16:10',
      read: false,
      status: 'approved',
      statusText: '已通过'
    },
    {
      id: 8,
      title: '报销单审批',
      content: '您的报销单（金额：2580.50元）正在审批中，请耐心等待。',
      time: '2024-01-14 14:20',
      read: true,
      status: 'pending',
      statusText: '审批中'
    },
    {
      id: 9,
      title: '加班申请结果',
      content: '您的加班申请（1月12日18:00-21:00）已被驳回，原因：未提前提交申请。',
      time: '2024-01-13 09:45',
      read: true,
      status: 'rejected',
      statusText: '已驳回'
    }
  ],
  notice: [
    {
      id: 10,
      title: '员工生日会通知',
      content: '本月员工生日会将于1月25日中午12:00在员工餐厅举行，欢迎大家参加！',
      time: '2024-01-15 11:30',
      read: false
    },
    {
      id: 11,
      title: '防疫通知',
      content: '近期疫情反弹，请大家做好个人防护，进入办公区域请佩戴口罩。',
      time: '2024-01-14 10:15',
      read: true
    },
    {
      id: 12,
      title: '公司团建活动',
      content: '公司将于本月28日组织团建活动，地点：阳光农场，请提前安排好工作。',
      time: '2024-01-13 15:40',
      read: false
    }
  ]
}

// 个人中心页面统计数据
export const profileStats = [
  {
    label: '考勤天数',
    value: '22'
  },
  {
    label: '请假次数',
    value: '2'
  },
  {
    label: '加班时长',
    value: '48h'
  },
  {
    label: '项目参与',
    value: '5'
  }
]

// 个人中心功能菜单
export const profileMenus = {
  personal: [
    {
      name: '个人信息',
      icon: 'account',
      desc: '查看并编辑个人资料',
      action: 'profile'
    },
    {
      name: '我的考勤',
      icon: 'clock',
      desc: '查看考勤记录和统计',
      action: 'attendance'
    },
    {
      name: '我的申请',
      icon: 'file-text',
      desc: '查看申请单据和审批状态',
      badge: '3',
      action: 'applications'
    }
  ],
  work: [
    {
      name: '我的任务',
      icon: 'checkbox-mark',
      desc: '查看分配给我的任务',
      badge: '5',
      action: 'tasks'
    },
    {
      name: '我的项目',
      icon: 'folder',
      desc: '参与的项目和进度',
      action: 'projects'
    },
    {
      name: '工作汇报',
      icon: 'edit-pen',
      desc: '提交和查看工作汇报',
      action: 'reports'
    }
  ],
  resource: [
    {
      name: '收藏夹',
      icon: 'star',
      desc: '我收藏的内容',
      action: 'favorites'
    },
    {
      name: '下载中心',
      icon: 'download',
      desc: '查看和管理已下载内容',
      action: 'downloads'
    },
    {
      name: '帮助中心',
      icon: 'question-circle',
      desc: '获取帮助和支持',
      action: 'help'
    }
  ]
}