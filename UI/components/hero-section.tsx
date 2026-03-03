import Image from "next/image"
import { Button } from "@/components/ui/button"
import { ArrowRight } from "lucide-react"

export function HeroSection() {
  return (
    <section className="relative overflow-hidden bg-secondary">
      <div className="mx-auto flex max-w-7xl flex-col-reverse items-center gap-8 px-4 py-16 md:py-24 lg:flex-row lg:gap-16 lg:px-8">
        {/* Text content */}
        <div className="flex flex-1 flex-col items-center text-center lg:items-start lg:text-left">
          <span className="mb-4 inline-block rounded-full border border-accent/30 bg-accent/10 px-4 py-1.5 text-xs font-semibold uppercase tracking-wider text-accent">
            New Collection 2026
          </span>
          <h1 className="text-balance font-serif text-4xl leading-tight tracking-tight text-foreground md:text-5xl lg:text-6xl">
            Discover Your Next Favorite Book
          </h1>
          <p className="mt-4 max-w-lg text-pretty text-base leading-relaxed text-muted-foreground md:text-lg">
            Explore our hand-picked collection of bestsellers, timeless classics,
            and hidden gems. Free shipping on every order over $35.
          </p>
          <div className="mt-8 flex flex-wrap items-center gap-4">
            <Button size="lg" className="gap-2 rounded-xl text-base">
              Shop Now
              <ArrowRight className="size-4" />
            </Button>
            <Button variant="outline" size="lg" className="rounded-xl text-base">
              Browse Categories
            </Button>
          </div>
          <div className="mt-8 flex items-center gap-8 text-sm text-muted-foreground">
            <div className="flex flex-col items-center lg:items-start">
              <span className="text-xl font-bold text-foreground">50K+</span>
              <span>Books Available</span>
            </div>
            <div className="h-8 w-px bg-border" />
            <div className="flex flex-col items-center lg:items-start">
              <span className="text-xl font-bold text-foreground">12K+</span>
              <span>Happy Readers</span>
            </div>
            <div className="h-8 w-px bg-border" />
            <div className="flex flex-col items-center lg:items-start">
              <span className="text-xl font-bold text-foreground">Free</span>
              <span>{'Shipping $35+'}</span>
            </div>
          </div>
        </div>

        {/* Hero image */}
        <div className="relative flex-1">
          <div className="relative aspect-[4/3] w-full max-w-lg overflow-hidden rounded-2xl shadow-2xl lg:max-w-none">
            <Image
              src="/images/hero-books.jpg"
              alt="A curated stack of books on a wooden table with coffee"
              fill
              className="object-cover"
              priority
              sizes="(max-width: 768px) 100vw, 50vw"
            />
          </div>
        </div>
      </div>
    </section>
  )
}
