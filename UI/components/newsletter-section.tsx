"use client"

import { Send } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"

export function NewsletterSection() {
  return (
    <section className="bg-primary py-16 md:py-20">
      <div className="mx-auto max-w-2xl px-4 text-center lg:px-8">
        <h2 className="font-serif text-3xl tracking-tight text-primary-foreground md:text-4xl">
          Stay in the Loop
        </h2>
        <p className="mt-3 text-sm leading-relaxed text-primary-foreground/70">
          Get weekly book recommendations, exclusive discounts, and early access
          to new arrivals delivered to your inbox.
        </p>
        <form
          className="mt-8 flex flex-col gap-3 sm:flex-row sm:gap-0"
          onSubmit={(e) => e.preventDefault()}
        >
          <Input
            type="email"
            placeholder="Enter your email address"
            className="h-12 flex-1 rounded-xl border-primary-foreground/20 bg-primary-foreground/10 text-primary-foreground placeholder:text-primary-foreground/50 focus-visible:border-accent focus-visible:ring-accent sm:rounded-r-none"
            required
          />
          <Button
            type="submit"
            size="lg"
            className="gap-2 rounded-xl bg-accent text-accent-foreground hover:bg-accent/90 sm:rounded-l-none"
          >
            <Send className="size-4" />
            Subscribe
          </Button>
        </form>
        <p className="mt-3 text-xs text-primary-foreground/50">
          No spam, ever. Unsubscribe anytime.
        </p>
      </div>
    </section>
  )
}
