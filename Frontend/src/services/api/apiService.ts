import API_BASE_URL, { getHeaders } from './apiConfig';

export const apiFetch = async (endpoint: string, options: RequestInit = {}) => {
  const url = endpoint.startsWith('http') ? endpoint : `${API_BASE_URL}${endpoint}`;
  
  const headers = {
    ...getHeaders(),
    ...(options.headers || {}),
  } as Record<string, string>;

  // Si enviamos FormData, el navegador debe establecer el Content-Type automáticamente con el boundary
  if (options.body instanceof FormData) {
    delete headers['Content-Type'];
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (response.status === 401 || response.status === 403) {
    // Manejo de expiración de token o falta de permisos
    // Podrías redirigir al login si es un 401
    // window.location.href = '/login';
  }

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || `Error HTTP: ${response.status}`);
  }

  return response;
};

export const apiJson = async (endpoint: string, options: RequestInit = {}) => {
  const response = await apiFetch(endpoint, options);
  if (response.status === 204) return null;
  return response.json();
};
