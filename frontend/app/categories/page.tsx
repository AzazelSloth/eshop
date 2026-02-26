'use client';

import Link from 'next/link';
import { useCategories } from '@/lib/hooks';

export default function CategoriesPage() {
  const { categories, loading, error } = useCategories(true);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="mb-8 text-3xl font-bold">Shop by Category</h1>

      {loading ? (
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          {[1, 2, 3, 4].map((i) => (
            <div key={i} className="animate-pulse rounded-lg border p-8">
              <div className="h-8 w-32 rounded bg-gray-200"></div>
              <div className="mt-4 h-4 w-full rounded bg-gray-200"></div>
            </div>
          ))}
        </div>
      ) : error ? (
        <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-600">
          {error}
        </div>
      ) : categories.length > 0 ? (
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
                {category.description && (
                  <p className="mt-2 text-gray-600">{category.description}</p>
                )}
                <span className="mt-4 inline-block font-medium transition-colors group-hover:text-primary">
                  Shop Now →
                </span>
              </div>
              <div className="absolute inset-0 bg-gradient-to-br from-gray-50 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />
            </Link>
          ))}
        </div>
      ) : (
        <p className="text-gray-600">No categories available</p>
      )}
    </div>
  );
}
