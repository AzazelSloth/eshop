'use client';

import Link from 'next/link';
import { useParams } from 'next/navigation';
import ProductCard from '@/components/ProductCard';
import { useCategory, useProducts } from '@/lib/hooks';

export default function CategoryDetailPage() {
  const params = useParams();
  const categoryId = params.id as string;
  
  const { category, loading: categoryLoading, error: categoryError } = useCategory(categoryId);
  const { products, loading: productsLoading, error: productsError } = useProducts({ 
    active: true,
    categoryId 
  });

  return (
    <div className="container mx-auto px-4 py-8">
      <nav className="mb-6 text-sm text-gray-600">
        <Link href="/" className="hover:text-gray-900">Home</Link>
        <span className="mx-2">/</span>
        <Link href="/categories" className="hover:text-gray-900">Categories</Link>
        <span className="mx-2">/</span>
        <span className="text-gray-900">
          {categoryLoading ? 'Loading...' : category?.name || 'Category'}
        </span>
      </nav>

      {categoryError ? (
        <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-600">
          {categoryError}
        </div>
      ) : categoryLoading ? (
        <div className="mb-8">
          <div className="h-10 w-48 animate-pulse rounded bg-gray-200"></div>
        </div>
      ) : category ? (
        <div className="mb-8">
          <h1 className="text-3xl font-bold">{category.name}</h1>
          {category.description && (
            <p className="mt-2 text-gray-600">{category.description}</p>
          )}
        </div>
      ) : null}

      {productsError ? (
        <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-600">
          {productsError}
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
      ) : products.length > 0 ? (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {products.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      ) : (
        <p className="text-gray-600">No products in this category yet.</p>
      )}
    </div>
  );
}
