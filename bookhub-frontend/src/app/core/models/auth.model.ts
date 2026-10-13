export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  username: string;
  type: string;
}

export interface CurrentUser {
  username: string;
  roles: string[];
}
