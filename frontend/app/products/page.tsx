'use client';

import { useState } from 'react';
import ProductCard from '@/components/ProductCard';
import { Product, Category } from '@/lib/types';

// Mock data - in production, this would come from the API
const allProducts: Product[] = [
  {
    id: 1,
    name: 'Premium Wireless Headphones',
    description: 'High-quality wireless headphones with noise cancellation',
    price: 299.99,
    stock: 15,
    imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400',
    category: { id: 1, name: 'Electronics' },
  },
  {
    id: 2,
    name: 'Smart Watch Pro',
    description: 'Advanced smartwatch with health monitoring features',
    price: 449.99,
    stock: 20,
    imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400',
    category: { id: 1, name: 'Electronics' },
  },
  {
    id: 3,
    name: 'Designer Sunglasses',
    description: 'Stylish sunglasses with UV protection',
    price: 159.99,
    stock: 30,
    imageUrl: 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400',
    category: { id: 2, name: 'Fashion' },
  },
  {
    id: 4,
    name: 'Leather Wallet',
    description: 'Genuine leather wallet with multiple compartments',
    price: 89.99,
    stock: 25,
    imageUrl: 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=400',
    category: { id: 2, name: 'Fashion' },
  },
  {
    id: 5,
    name: 'Running Shoes',
    description: 'Comfortable running shoes with excellent cushioning',
    price: 129.99,
    stock: 18,
    imageUrl: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400',
    category: { id: 4, name: 'Sports' },
  },
  {
    id: 6,
    name: 'Coffee Maker',
    description: 'Premium coffee maker with multiple brewing options',
    price: 199.99,
    stock: 12,
    imageUrl: 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=400',
    category: { id: 3, name: 'Home & Garden' },
  },
  {
    id: 7,
    name: 'Bluetooth Speaker',
    description: 'Portable bluetooth speaker with rich sound',
    price: 79.99,
    stock: 35,
    imageUrl: 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400',
    category: { id: 1, name: 'Electronics' },
  },
  {
    id: 8,
    name: 'Yoga Mat',
    description: 'Non-slip yoga mat with carrying strap',
    price: 49.99,
    stock: 40,
    imageUrl: 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400',
    category: { id: 4, name: 'Sports' },
  },
];

const categories: Category[] = [
  { id: 1, name: 'All', description: '' },
  { id: 1, name: 'Electronics', description: 'Latest gadgets and devices' },
  { id: 2, name: 'Fashion', description: 'Trendy clothing and accessories' },
  { id: 3, name: 'Home & Garden', description: 'Everything for your home' },
  { id: 4, name: 'Sports', description: 'Sports equipment and gear' },
];

export default function ProductsPage() {
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [sortBy, setSortBy] = useState<string>('name');

  const filteredProducts = allProducts
    .filter((product) => 
      selectedCategory === 'All' || product.category?.name === selectedCategory
    )
    .sort((a, b) => {
      if (sortBy === 'price-low') return a.price - b.price;
      if (sortBy === 'price-high') return b.price - a.price;
      return a.name.localeCompare(b.name);
    });

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="mb-8 text-3xl font-bold">All Products</h1>

      <div className="flex flex-col gap-6 lg:flex-row">
        {/* Filters Sidebar */}
        <aside className="w-full lg:w-64">
          <div className="rounded-lg border p-4">
            <h2 className="mb-4 text-lg font-semibold">Filters</h2>
            
            <div className="mb-6">
              <h3 className="mb-2 text-sm font-medium">Category</h3>
              <div className="space-y-2">
                {categories.slice(1).map((category) => (
                  <label key={category.id} className="flex items-center gap-2">
                    <input
                      type="radio"
                      name="category"
                      checked={selectedCategory === category.name}
                      onChange={() => setSelectedCategory(category.name)}
                      className="h-4 w-4"
                    />
                    <span className="text-sm">{category.name}</span>
                  </label>
                ))}
                <label className="flex items-center gap-2">
                  <input
                    type="radio"
                    name="category"
                    checked={selectedCategory === 'All'}
                    onChange={() => setSelectedCategory('All')}
                    className="h-4 w-4"
                  />
                  <span className="text-sm">All Categories</span>
                </label>
              </div>
            </div>

            <div>
              <h3 className="mb-2 text-sm font-medium">Sort By</h3>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="w-full rounded-md border p-2 text-sm"
              >
                <option value="name">Name (A-Z)</option>
                <option value="price-low">Price (Low to High)</option>
                <option value="price-high">Price (High to Low)</option>
              </select>
            </div>
          </div>
        </aside>

        {/* Products Grid */}
        <div className="flex-1">
          <div className="mb-4 text-sm text-gray-600">
            Showing {filteredProducts.length} products
          </div>
          
          {filteredProducts.length > 0 ? (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
              {filteredProducts.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          ) : (
            <div className="py-12 text-center text-gray-600">
              No products found in this category.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
