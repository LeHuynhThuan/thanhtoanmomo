"use client"

import { useState } from "react"
import { BookCard, type Book } from "@/components/book-card"
import { Button } from "@/components/ui/button"

const books: Book[] = [
  {
    id: 1,
    title: "The Silent Echo",
    author: "Amara Okafor",
    price: 16.99,
    originalPrice: 24.99,
    rating: 4.5,
    reviews: 342,
    cover: "/images/books/fiction-1.jpg",
    badge: "Best Seller",
  },
  {
    id: 2,
    title: "Midnight Garden",
    author: "Julian Voss",
    price: 14.99,
    rating: 4,
    reviews: 218,
    cover: "/images/books/fiction-2.jpg",
  },
  {
    id: 3,
    title: "The Growth Mindset",
    author: "Dr. Helen Park",
    price: 19.99,
    originalPrice: 27.99,
    rating: 5,
    reviews: 512,
    cover: "/images/books/business-1.jpg",
    badge: "Editor's Pick",
  },
  {
    id: 4,
    title: "Digital Leaders",
    author: "Marcus Chen",
    price: 22.49,
    rating: 4,
    reviews: 175,
    cover: "/images/books/business-2.jpg",
  },
  {
    id: 5,
    title: "Learning Reimagined",
    author: "Prof. Sarah Lin",
    price: 18.99,
    rating: 4.5,
    reviews: 289,
    cover: "/images/books/education-1.jpg",
  },
  {
    id: 6,
    title: "The Art of Thinking",
    author: "Rohan Mehta",
    price: 15.99,
    originalPrice: 21.99,
    rating: 4,
    reviews: 194,
    cover: "/images/books/education-2.jpg",
    badge: "New",
  },
  {
    id: 7,
    title: "The Last Chapter",
    author: "Elena Rossi",
    price: 13.99,
    rating: 4.5,
    reviews: 406,
    cover: "/images/books/fiction-3.jpg",
    badge: "Best Seller",
  },
  {
    id: 8,
    title: "Atomic Focus",
    author: "James Liu",
    price: 17.49,
    originalPrice: 24.00,
    rating: 5,
    reviews: 623,
    cover: "/images/books/business-3.jpg",
  },
]

const filters = ["All", "Fiction", "Business", "Education"]

export function ProductsSection() {
  const [activeFilter, setActiveFilter] = useState("All")

  return (
    <section id="products" className="bg-secondary py-16 md:py-24">
      <div className="mx-auto max-w-7xl px-4 lg:px-8">
        {/* Header */}
        <div className="mb-10 flex flex-col items-center justify-between gap-4 md:flex-row">
          <div>
            <span className="mb-2 inline-block text-sm font-semibold uppercase tracking-wider text-accent">
              Our Collection
            </span>
            <h2 className="font-serif text-3xl tracking-tight text-foreground md:text-4xl">
              Popular Books
            </h2>
          </div>

          {/* Filter tabs */}
          <div className="flex gap-2">
            {filters.map((filter) => (
              <Button
                key={filter}
                variant={activeFilter === filter ? "default" : "outline"}
                size="sm"
                className="rounded-xl"
                onClick={() => setActiveFilter(filter)}
              >
                {filter}
              </Button>
            ))}
          </div>
        </div>

        {/* Product grid */}
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
          {books.map((book) => (
            <BookCard key={book.id} book={book} />
          ))}
        </div>

        {/* Load more */}
        <div className="mt-12 text-center">
          <Button variant="outline" size="lg" className="rounded-xl">
            View All Books
          </Button>
        </div>
      </div>
    </section>
  )
}
