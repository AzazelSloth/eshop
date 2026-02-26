'use client';

import Link from 'next/link';

export default function Footer() {
  return (
    <footer className="border-t bg-white">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 gap-8 md:grid-cols-4">
          {/* Brand */}
          <div>
            <Link href="/" className="text-xl font-bold">
              MyStars
            </Link>
            <p className="mt-2 text-sm text-gray-600">
              Your favorite online shop for premium products
            </p>
          </div>

          {/* Shop Links */}
          <div>
            <h3 className="font-semibold">Shop</h3>
            <ul className="mt-4 space-y-2">
              <li>
                <Link href="/products" className="text-sm text-gray-600 hover:text-black">
                  All Products
                </Link>
              </li>
              <li>
                <Link href="/categories" className="text-sm text-gray-600 hover:text-black">
                  Categories
                </Link>
              </li>
            </ul>
          </div>

          {/* Account Links */}
          <div>
            <h3 className="font-semibold">Account</h3>
            <ul className="mt-4 space-y-2">
              <li>
                <Link href="/login" className="text-sm text-gray-600 hover:text-black">
                  Sign In
                </Link>
              </li>
              <li>
                <Link href="/cart" className="text-sm text-gray-600 hover:text-black">
                  Cart
                </Link>
              </li>
              <li>
                <Link href="/orders" className="text-sm text-gray-600 hover:text-black">
                  Orders
                </Link>
              </li>
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h3 className="font-semibold">Contact</h3>
            <ul className="mt-4 space-y-2">
              <li className="text-sm text-gray-600">Email: support@mystars.com</li>
              <li className="text-sm text-gray-600">Phone: +1 234 567 890</li>
            </ul>
          </div>
        </div>

        <div className="mt-8 border-t pt-8 text-center text-sm text-gray-600">
          © {new Date().getFullYear()} MyStars. All rights reserved.
        </div>
      </div>
    </footer>
  );
}
