'use client';

import Link from 'next/link';
import { useCart } from '@/lib/cart-context';
import { useState } from 'react';

export default function Header() {
  const { totalItems } = useCart();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/60">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <Link href="/" className="flex items-center space-x-2">
          <span className="text-xl font-bold">MyStars</span>
        </Link>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center space-x-6">
          <Link href="/" className="text-sm font-medium transition-colors hover:text-primary">
            Home
          </Link>
          <Link href="/products" className="text-sm font-medium transition-colors hover:text-primary">
            Products
          </Link>
          <Link href="/categories" className="text-sm font-medium transition-colors hover:text-primary">
            Categories
          </Link>
        </nav>

        {/* Cart and Menu */}
        <div className="flex items-center space-x-4">
          <Link href="/cart" className="relative flex items-center text-sm font-medium transition-colors hover:text-primary">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"
              />
            </svg>
            {totalItems > 0 && (
              <span className="absolute -top-2 -right-2 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-xs text-white">
                {totalItems}
              </span>
            )}
            <span className="ml-1">Cart</span>
          </Link>

          <Link href="/login" className="hidden text-sm font-medium transition-colors hover:text-primary md:inline-block">
            Sign In
          </Link>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
            aria-label="Toggle menu"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              {isMenuOpen ? (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              ) : (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              )}
            </svg>
          </button>
        </div>
      </div>

      {/* Mobile Navigation */}
      {isMenuOpen && (
        <nav className="border-t md:hidden">
          <div className="container mx-auto px-4 py-4 space-y-3">
            <Link href="/" className="block text-sm font-medium" onClick={() => setIsMenuOpen(false)}>
              Home
            </Link>
            <Link href="/products" className="block text-sm font-medium" onClick={() => setIsMenuOpen(false)}>
              Products
            </Link>
            <Link href="/categories" className="block text-sm font-medium" onClick={() => setIsMenuOpen(false)}>
              Categories
            </Link>
            <Link href="/login" className="block text-sm font-medium" onClick={() => setIsMenuOpen(false)}>
              Sign In
            </Link>
          </div>
        </nav>
      )}
    </header>
  );
}
