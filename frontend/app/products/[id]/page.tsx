'use client';

import Image from 'next/image';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { useState } from 'react';
import { useCart } from '@/lib/cart-context';
import { Product } from '@/lib/types';

// Mock data - in production, this would come from the API
const productsData: Record<number, Product> = {
  1: {
    id: 1,
    name: 'Premium Wireless Headphones',
    description: 'Experience crystal-clear audio with our premium wireless headphones. Featuring advanced noise cancellation technology, 30-hour battery life, and premium comfort for extended listening sessions. The intuitive touch controls allow you to manage your music, calls, and voice assistant with ease.',
    price: 299.99,
    stock: 15,
    imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600',
    category: { id: 1, name: 'Electronics' },
  },
  2: {
    id: 2,
    name: 'Smart Watch Pro',
    description: 'Stay connected and healthy with our Smart Watch Pro. Features include heart rate monitoring, sleep tracking, GPS, water resistance, and a stunning AMOLED display. Compatible with both iOS and Android devices.',
    price: 449.99,
    stock: 20,
    imageUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600',
    category: { id: 1, name: 'Electronics' },
  },
  3: {
    id: 3,
    name: 'Designer Sunglasses',
    description: 'Make a statement with these designer sunglasses. Premium UV400 protection, scratch-resistant lenses, and a lightweight frame for all-day comfort. Perfect for any occasion.',
    price: 159.99,
    stock: 30,
    imageUrl: 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=600',
    category: { id: 2, name: 'Fashion' },
  },
  4: {
    id: 4,
    name: 'Leather Wallet',
    description: 'Crafted from genuine leather, this wallet features multiple compartments for cards, cash, and ID. The RFID blocking technology keeps your information secure.',
    price: 89.99,
    stock: 25,
    imageUrl: 'https://images.unsplash.com/photo-1627123424574-724758594e93?w=600',
    category: { id: 2, name: 'Fashion' },
  },
};

export default function ProductDetailPage() {
  const params = useParams();
  const { addToCart } = useCart();
  const [quantity, setQuantity] = useState(1);
  
  const productId = Number(params.id);
  const product = productsData[productId];

  if (!product) {
    return (
      <div className="container mx-auto px-4 py-16">
        <h1 className="text-2xl font-bold">Product Not Found</h1>
        <Link href="/products" className="mt-4 text-primary hover:underline">
          Back to Products
        </Link>
      </div>
    );
  }

  const handleAddToCart = () => {
    addToCart(product, quantity);
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <nav className="mb-6 text-sm text-gray-600">
        <Link href="/" className="hover:text-gray-900">Home</Link>
        <span className="mx-2">/</span>
        <Link href="/products" className="hover:text-gray-900">Products</Link>
        <span className="mx-2">/</span>
        <span className="text-gray-900">{product.name}</span>
      </nav>

      <div className="grid grid-cols-1 gap-8 lg:grid-cols-2">
        {/* Product Image */}
        <div className="relative aspect-square overflow-hidden rounded-lg bg-gray-100">
          {product.imageUrl ? (
            <Image
              src={product.imageUrl}
              alt={product.name}
              fill
              className="object-cover"
              priority
            />
          ) : (
            <div className="flex h-full w-full items-center justify-center text-gray-400">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-24 w-24"
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
              <span className="rounded-full bg-red-500 px-6 py-2 font-semibold text-white">
                Out of Stock
              </span>
            </div>
          )}
        </div>

        {/* Product Details */}
        <div>
          {product.category && (
            <p className="text-sm text-gray-500">{product.category.name}</p>
          )}
          <h1 className="mt-2 text-3xl font-bold">{product.name}</h1>
          <p className="mt-4 text-2xl font-bold">${product.price.toFixed(2)}</p>
          
          <p className="mt-6 text-gray-600">{product.description}</p>

          <div className="mt-6">
            <p className="text-sm text-gray-600">
              {product.stock > 0 ? (
                <span className="text-green-600">In Stock ({product.stock} available)</span>
              ) : (
                <span className="text-red-600">Out of Stock</span>
              )}
            </p>
          </div>

          {product.stock > 0 && (
            <div className="mt-8">
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-2">
                  <label className="text-sm font-medium">Quantity:</label>
                  <div className="flex items-center">
                    <button
                      onClick={() => setQuantity(Math.max(1, quantity - 1))}
                      className="rounded-l-md border px-3 py-2 hover:bg-gray-100"
                    >
                      -
                    </button>
                    <span className="border px-4 py-2">{quantity}</span>
                    <button
                      onClick={() => setQuantity(Math.min(product.stock, quantity + 1))}
                      className="rounded-r-md border px-3 py-2 hover:bg-gray-100"
                    >
                      +
                    </button>
                  </div>
                </div>
              </div>

              <button
                onClick={handleAddToCart}
                className="mt-6 w-full rounded-full bg-black py-4 font-semibold text-white transition-colors hover:bg-gray-800"
              >
                Add to Cart - ${(product.price * quantity).toFixed(2)}
              </button>
            </div>
          )}

          <div className="mt-8 space-y-4 border-t pt-6">
            <div className="flex items-center gap-3 text-sm text-gray-600">
              <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4" />
              </svg>
              <span>Free shipping on orders over $50</span>
            </div>
            <div className="flex items-center gap-3 text-sm text-gray-600">
              <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              <span>30-day return policy</span>
            </div>
            <div className="flex items-center gap-3 text-sm text-gray-600">
              <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
              </svg>
              <span>Secure payment</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
