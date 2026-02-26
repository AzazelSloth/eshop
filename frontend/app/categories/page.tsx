'use client';

import Link from 'next/link';
import { Category } from '@/lib/types';

// Mock data - in production, this would come from the API
const categories: Category[] = [
  { 
    id: 1, 
    name: 'Electronics', 
    description: 'Explore the latest gadgets and cutting-edge technology. From smartphones to smart home devices, find everything you need to stay connected.' 
  },
  { 
    id: 2, 
    name: 'Fashion', 
    description: 'Discover trendy clothing, stylish accessories, and premium footwear. Express your unique style with our curated collection.' 
  },
  { 
    id: 3, 
    name: 'Home & Garden', 
    description: 'Transform your living space with our wide range of home decor, furniture, and garden essentials.' 
  },
  { 
    id: 4, 
    name: 'Sports', 
    description: 'Find high-quality sports equipment, fitness gear, and outdoor adventure supplies for an active lifestyle.' 
  },
];

export default function CategoriesPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="mb-8 text-3xl font-bold">Shop by Category</h1>

      <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
        {categories.map((category) => (
          <Link
            key={category.id}
            href={`/categories/${category.id}`}
            className="group relative overflow-hidden rounded-lg border p-8 transition-all hover:shadow-lg"
          >
            <div className="relative z-10">
              <h2 className="text-2xl font-bold group-hover:text-primary">
                {category.name}
              </h2>
              <p className="mt-2 text-gray-600">{category.description}</p>
              <span className="mt-4 inline-block font-medium transition-colors group-hover:text-primary">
                Shop Now →
              </span>
            </div>
            <div className="absolute inset-0 bg-gradient-to-br from-gray-50 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />
          </Link>
        ))}
      </div>
    </div>
  );
}
