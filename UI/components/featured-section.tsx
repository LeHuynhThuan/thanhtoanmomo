"use client"

import Image from "next/image"
import { Star, ShoppingCart, Award } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

const featured = [
  {
    id: 1,
    title: "The Silent Echo",
    author: "Amara Okafor",
    price: 16.99,
    rating: 4.5,
    reviews: 342,
    cover: "/images/books/fiction-1.jpg",
    description:
      "A haunting exploration of memory and identity that follows three generations of women navigating the echoes of unspoken truths.",
  },
  {
    id: 3,
    title: "The Growth Mindset",
    author: "Dr. Helen Park",
    price: 19.99,
    rating: 5,
    reviews: 512,
    cover: "/images/books/business-1.jpg",
    description:
      "The definitive guide to cultivating a growth-oriented approach to business and life, backed by a decade of research.",
  },
  {
    id: 7,
    title: "The Last Chapter",
    author: "Elena Rossi",
    price: 13.99,
    rating: 4.5,
    reviews: 406,
    cover: "/images/books/fiction-3.jpg",
    description:
      "A gripping mystery that keeps you turning pages until the very last one. Winner of the International Thriller Writers Award.",
  },
  {
    id: 8,
    title: "Atomic Focus",
    author: "James Liu",
    price: 17.49,
    rating: 5,
    reviews: 623,
    cover: "/images/books/business-3.jpg",
    description:
      "Master the art of deep concentration in a distracted world. Practical strategies that top performers swear by.",
  },
]

export function FeaturedSection() {
  return (
    <section id="featured" className="bg-background py-16 md:py-24">
      <div className="mx-auto max-w-7xl px-4 lg:px-8">
        {/* Header */}
        <div className="mb-12 text-center">
          <span className="mb-2 inline-block text-sm font-semibold uppercase tracking-wider text-accent">
            Handpicked for You
          </span>
          <h2 className="text-balance font-serif text-3xl tracking-tight text-foreground md:text-4xl">
            Featured Best Sellers
          </h2>
          <p className="mt-3 text-pretty text-muted-foreground">
            Loved by readers, recommended by our editors.
          </p>
        </div>

        {/* Featured grid */}
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          {featured.map((book) => (
            <article
              key={book.id}
              className="group flex flex-col overflow-hidden rounded-2xl border border-border bg-card shadow-sm transition-all hover:shadow-lg sm:flex-row"
            >
              {/* Cover */}
              <div className="relative aspect-[3/4] w-full shrink-0 overflow-hidden bg-secondary sm:w-44 md:w-52">
                <Image
                  src={book.cover}
                  alt={`Cover of ${book.title} by ${book.author}`}
                  fill
                  className="object-cover transition-transform duration-300 group-hover:scale-105"
                  sizes="(max-width: 640px) 100vw, 220px"
                />
                <Badge className="absolute left-3 top-3 gap-1 rounded-lg bg-accent text-accent-foreground">
                  <Award className="size-3" />
                  Best Seller
                </Badge>
              </div>

              {/* Content */}
              <div className="flex flex-1 flex-col justify-between p-5 sm:p-6">
                <div>
                  <h3 className="font-serif text-xl text-foreground">
                    {book.title}
                  </h3>
                  <p className="mt-1 text-sm text-muted-foreground">
                    by {book.author}
                  </p>

                  {/* Rating */}
                  <div className="mt-3 flex items-center gap-1">
                    {Array.from({ length: 5 }).map((_, i) => (
                      <Star
                        key={i}
                        className={`size-4 ${
                          i < Math.floor(book.rating)
                            ? "fill-accent text-accent"
                            : "fill-muted text-muted"
                        }`}
                      />
                    ))}
                    <span className="ml-1 text-xs text-muted-foreground">
                      {book.rating} ({book.reviews} reviews)
                    </span>
                  </div>

                  <p className="mt-3 line-clamp-2 text-sm leading-relaxed text-muted-foreground">
                    {book.description}
                  </p>
                </div>

                <div className="mt-4 flex items-center justify-between">
                  <span className="text-xl font-bold text-foreground">
                    ${book.price.toFixed(2)}
                  </span>
                  <Button
                    size="sm"
                    className="gap-2 rounded-xl"
                    aria-label={`Add ${book.title} to cart`}
                  >
                    <ShoppingCart className="size-4" />
                    Add to Cart
                  </Button>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  )
}
