import API_BASE_URL, { getHeaders } from './apiConfig';

export const apiFetch = async (endpoint: string, options: RequestInit = {}) => {
  const url = endpoint.startsWith('http') ? endpoint : `${API_BASE_URL}${endpoint}`;

  const headers = {
    ...getHeaders(),
    ...(options.headers ?? {}),
  } as Record<string, string>;

  // Si enviamos FormData, el navegador debe establecer el Content-Type automáticamente con el boundary
  if (options.body instanceof FormData) {
    delete headers['Content-Type'];
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    console.warn(`Petición no autorizada (401) a: ${url}. Redirigiendo a login...`);
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = '/login';
    throw new Error('Session expired');
  }

  if (response.status === 403) {
    // Forbidden: el usuario está logueado pero no tiene permisos para este recurso
    window.location.href = '/unauthorized';
    throw new Error('Forbidden');
  }

  if (!response.ok) {
    const errorData: Record<string, unknown> = await response.json().catch(() => ({}));
    const msg =
      typeof errorData.message === 'string' ? errorData.message : `Error HTTP: ${response.status}`;
    throw new Error(msg);
  }

  return response;
};

export const apiJson = async (endpoint: string, options: RequestInit = {}) => {
  const response = await apiFetch(endpoint, options);
  if (response.status === 204) return null;
  return response.json();
};
