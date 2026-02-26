'use client';

import { useState } from 'react';
import ProductCard from '@/components/ProductCard';
import { useProducts, useCategories } from '@/lib/hooks';

export default function ProductsPage() {
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [sortBy, setSortBy] = useState<string>('name');
  
  const { categories, loading: categoriesLoading } = useCategories(true);
  const { products, loading: productsLoading, error } = useProducts({ 
    active: true,
    categoryId: selectedCategory !== 'All' ? selectedCategory : undefined
  });

  const filteredProducts = [...products].sort((a, b) => {
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
              {categoriesLoading ? (
                <div className="space-y-2">
                  {[1, 2, 3, 4].map((i) => (
                    <div key={i} className="animate-pulse h-4 w-20 rounded bg-gray-200"></div>
                  ))}
                </div>
              ) : (
                <div className="space-y-2">
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
                  {categories.map((category) => (
                    <label key={category.id} className="flex items-center gap-2">
                      <input
                        type="radio"
                        name="category"
                        checked={selectedCategory === category.id}
                        onChange={() => setSelectedCategory(category.id)}
                        className="h-4 w-4"
                      />
                      <span className="text-sm">{category.name}</span>
                    </label>
                  ))}
                </div>
              )}
            </div>

            <div>
              <h3 className="mb-2 text-sm font-medium">Sort By</h3>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                className="w-full rounded-md border p-2 text-sm"
                aria-label="Sort products"
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
            {productsLoading ? 'Loading...' : `Showing ${filteredProducts.length} products`}
          </div>
          
          {error ? (
            <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-600">
              {error}
            </div>
          ) : productsLoading ? (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
              {[1, 2, 3, 4, 5, 6].map((i) => (
                <div key={i} className="animate-pulse overflow-hidden rounded-lg border bg-white">
                  <div className="aspect-square bg-gray-200"></div>
                  <div className="p-4">
                    <div className="h-4 w-16 rounded bg-gray-200"></div>
                    <div className="mt-2 h-6 w-48 rounded bg-gray-200"></div>
                    <div className="mt-2 h-4 w-full rounded bg-gray-200"></div>
                    <div className="mt-4 h-8 w-24 rounded bg-gray-200"></div>
                  </div>
                </div>
              ))}
            </div>
          ) : filteredProducts.length > 0 ? (
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
