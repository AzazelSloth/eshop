'use client';

import Image from 'next/image';
import Link from 'next/link';
import { Product } from '@/lib/types';
import { useCart } from '@/lib/cart-context';

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const { addToCart } = useCart();

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    addToCart(product, 1);
  };

  return (
    <Link href={`/products/${product.id}`} className="group">
      <div className="overflow-hidden rounded-lg border bg-white transition-shadow hover:shadow-md">
        <div className="relative aspect-square overflow-hidden bg-gray-100">
          {product.imageUrl ? (
            <Image
              src={product.imageUrl}
              alt={product.name}
              fill
              className="object-cover transition-transform group-hover:scale-105"
            />
          ) : (
            <div className="flex h-full w-full items-center justify-center text-gray-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-16 w-16"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={1}
                  d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
                />
              </svg>
            </div>
          )}
          {product.stock === 0 && (
            <div className="absolute inset-0 flex items-center justify-center bg-black/50">
              <span className="rounded-full bg-red-500 px-3 py-1 text-sm font-medium text-white">
                Out of Stock
              </span>
            </div>
          )}
        </div>
        <div className="p-4">
          {product.category && (
            <p className="text-xs text-gray-500">{product.category.name}</p>
          )}
          <h3 className="mt-1 text-lg font-semibold line-clamp-2">{product.name}</h3>
          <p className="mt-2 line-clamp-2 text-sm text-gray-600">{product.description}</p>
          <div className="mt-4 flex items-center justify-between">
            <span className="text-xl font-bold">${product.price.toFixed(2)}</span>
            <button
              onClick={handleAddToCart}
              disabled={product.stock === 0}
              className="rounded-full bg-black px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-gray-800 disabled:cursor-not-allowed disabled:opacity-50"
            >
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </Link>
  );
}
