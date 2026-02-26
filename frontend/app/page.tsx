'use client';

import Link from 'next/link';
import ProductCard from '@/components/ProductCard';
import { Product, Category } from '@/lib/types';

// Mock data for demonstration - in production, this would come from the API
const featuredProducts: Product[] = [
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
];

const categories: Category[] = [
  { id: 1, name: 'Electronics', description: 'Latest gadgets and devices' },
  { id: 2, name: 'Fashion', description: 'Trendy clothing and accessories' },
  { id: 3, name: 'Home & Garden', description: 'Everything for your home' },
  { id: 4, name: 'Sports', description: 'Sports equipment and gear' },
];

export default function Home() {
  return (
    <div className="flex flex-col">
      {/* Hero Section */}
      <section className="relative bg-gradient-to-r from-gray-900 to-gray-700 py-20 text-white">
        <div className="container mx-auto px-4">
          <div className="max-w-2xl">
            <h1 className="text-4xl font-bold md:text-6xl">
              Welcome to MyStars
            </h1>
            <p className="mt-4 text-lg text-gray-200">
              Discover amazing products at unbeatable prices. Shop the latest trends and enjoy a seamless shopping experience.
            </p>
            <div className="mt-8 flex gap-4">
              <Link
                href="/products"
                className="rounded-full bg-white px-6 py-3 font-semibold text-gray-900 transition-colors hover:bg-gray-100"
              >
                Shop Now
              </Link>
              <Link
                href="/categories"
                className="rounded-full border border-white px-6 py-3 font-semibold text-white transition-colors hover:bg-white/10"
              >
                Browse Categories
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Categories Section */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <h2 className="mb-8 text-3xl font-bold">Shop by Category</h2>
          <div className="grid grid-cols-2 gap-4 md:grid-cols-4">
            {categories.map((category) => (
              <Link
                key={category.id}
                href={`/categories/${category.id}`}
                className="group relative overflow-hidden rounded-lg border p-6 transition-shadow hover:shadow-lg"
              >
                <h3 className="text-lg font-semibold">{category.name}</h3>
                <p className="mt-2 text-sm text-gray-600">{category.description}</p>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Featured Products Section */}
      <section className="bg-gray-50 py-16">
        <div className="container mx-auto px-4">
          <div className="mb-8 flex items-center justify-between">
            <h2 className="text-3xl font-bold">Featured Products</h2>
            <Link
              href="/products"
              className="text-sm font-medium text-primary hover:underline"
            >
              View All Products →
            </Link>
          </div>
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {featuredProducts.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
            <div className="text-center">
              <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-gray-100">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"
                  />
                </svg>
              </div>
              <h3 className="text-lg font-semibold">Free Shipping</h3>
              <p className="mt-2 text-sm text-gray-600">
                On orders over $50
              </p>
            </div>
            <div className="text-center">
              <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-gray-100">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"
                  />
                </svg>
              </div>
              <h3 className="text-lg font-semibold">Secure Payment</h3>
              <p className="mt-2 text-sm text-gray-600">
                100% secure payment
              </p>
            </div>
            <div className="text-center">
              <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-gray-100">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                  />
                </svg>
              </div>
              <h3 className="text-lg font-semibold">Easy Returns</h3>
              <p className="mt-2 text-sm text-gray-600">
                30-day return policy
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
