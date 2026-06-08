export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  surname: string;
  nif: string;
  fechaNacimiento: string;
}

export interface JwtPayload {
  sub: string;
  role: string;
  name?: string;
  surname?: string;
  iat: number;
  exp: number;
}

export type UserRole = 'ADMIN' | 'MEDICO' | 'PACIENTE';
