declare namespace API {

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: string;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type BaseResponseboolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseInterfaceInfo = {
    code?: number;
    data?: InterfaceInfo;
    message?: string;
  };

  type BaseResponseListInterfaceInfoVO = {
    code?: number;
    data?: InterfaceInfoVO[];
    message?: string;
  };

  type BaseResponselong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseobject = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponsePageInterfaceInfo = {
    code?: number;
    data?: PageInterfaceInfo;
    message?: string;
  };

  type BaseResponseUser = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type DeleteRequest = {
    id?: string;
  };

  type getInterfaceInfoByIdUsingGETParams = {
    /** id */
    id?: string;
  };

  type IdRequest = {
    id?: string;
  };

  type InterfaceInfo = {
    createTime?: string;
    description?: string;
    id?: string;
    isDelete?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParam?: string;
    requestBody?: string;
    responseHeader?: string;
    responseBody?: string;
    status?: string;
    updateTime?: string;
    url?: string;
    userid?: string;
  };

  type InterfaceInfoAddRequest = {
    description?: string;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParam?: string;
    requestBody?: string;
    responseHeader?: string;
    responseBody?: string;
    url?: string;
  };

  type InterfaceInfoInvokeRequest = {
    id?: string;
    userrequestParam?: string;
  };

  type InterfaceInfoUpdateRequest = {
    description?: string;
    id?: string;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParam?: string;
    requestBody?: string;
    responseHeader?: string;
    responseBody?: string;
    status?: string;
    url?: string;
  };

  type InterfaceInfoVO = {
    createTime?: string;
    description?: string;
    id?: string;
    isDelete?: number;
    method?: string;
    name?: string;
    requestHeader?: string;
    requestParam?: string;
    requestBody?: string;
    responseHeader?: string;
    responseBody?: string;
    status?: string;
    totalNum?: number;
    updateTime?: string;
    url?: string;
    userid?: string;
  };

  type listInterfaceInfoByPageUsingGETParams = {
    current?: number;
    description?: string;
    id?: string;
    method?: string;
    name?: string;
    pageSize?: number;
    requestHeader?: string;
    requestParam?: string;
    requestBody?: string;
    responseHeader?: string;
    responseBody?: string;
    sortField?: string;
    sortOrder?: string;
    status?: string;
    url?: string;
    userid?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageInterfaceInfo = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: InterfaceInfo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type User = {
    accessKey?: string;
    createTime?: string;
    gender?: number;
    id?: string;
    isDelete?: number;
    secretKey?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    token?: string;
  };

  type UserAddRequest = {
    gender?: number;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserInterfaceInfo = {
    createTime?: string;
    id?: string;
    interfaceInfoid?: string;
    isDelete?: number;
    leftNum?: number;
    status?: string;
    totalNum?: number;
    updateTime?: string;
    userid?: string;
  };

  type UserInterfaceInfoAddRequest = {
    interfaceInfoid?: string;
    leftNum?: number;
    totalNum?: number;
    userid?: string;
  };

  type UserInterfaceInfoUpdateRequest = {
    id?: string;
    leftNum?: number;
    status?: string;
    totalNum?: number;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateRequest = {
    gender?: number;
    id?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    gender?: number;
    id?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

}
