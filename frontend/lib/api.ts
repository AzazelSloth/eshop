import { Product, Category, User, Order, ApiResponse, PaginatedResponse } from './types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;
    
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    // Add auth token if available
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('auth_token');
      if (token) {
        (headers as Record<string, string>)['Authorization'] = `Bearer ${token}`;
      }
    }

    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'An error occurred' }));
      throw new Error(error.message || `HTTP error! status: ${response.status}`);
    }

    return response.json();
  }

  // Products
  async getProducts(page = 0, size = 20): Promise<PaginatedResponse<Product>> {
    return this.request(`/products?page=${page}&size=${size}`);
  }

  async getProduct(id: number): Promise<Product> {
    return this.request(`/products/${id}`);
  }

  async getProductsByCategory(categoryId: number): Promise<Product[]> {
    return this.request(`/products?categoryId=${categoryId}`);
  }

  async searchProducts(query: string): Promise<Product[]> {
    return this.request(`/products/search?q=${encodeURIComponent(query)}`);
  }

  // Categories
  async getCategories(): Promise<Category[]> {
    return this.request('/categories');
  }

  async getCategory(id: number): Promise<Category> {
    return this.request(`/categories/${id}`);
  }

  // Auth
  async login(email: string, password: string): Promise<{ token: string; user: User }> {
    const response = await this.request<{ token: string; user: User }>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    
    if (typeof window !== 'undefined') {
      localStorage.setItem('auth_token', response.token);
    }
    
    return response;
  }

  async register(email: string, password: string, firstName: string, lastName: string): Promise<{ token: string; user: User }> {
    const response = await this.request<{ token: string; user: User }>('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, firstName, lastName }),
    });
    
    if (typeof window !== 'undefined') {
      localStorage.setItem('auth_token', response.token);
    }
    
    return response;
  }

  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('auth_token');
    }
  }

  // Orders
  async getOrders(): Promise<Order[]> {
    return this.request('/orders');
  }

  async getOrder(id: number): Promise<Order> {
    return this.request(`/orders/${id}`);
  }

  async createOrder(items: { productId: number; quantity: number }[]): Promise<Order> {
    return this.request('/orders', {
      method: 'POST',
      body: JSON.stringify({ items }),
    });
  }
}

export const api = new ApiClient(API_BASE_URL);
export default api;
