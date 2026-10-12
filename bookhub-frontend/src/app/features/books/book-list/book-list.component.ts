import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookService } from '../../../core/services/book.service';
import { Book } from '../../../core/models/book.model';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  private bookService = inject(BookService);

  books: Book[] = [];
  total = 0;
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.bookService.getAll().subscribe({
      next: (page) => {
        this.books = page.content;
        this.total = page.totalElements;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.error = `Erreur ${err.status}: ${err.message}`;
        this.loading = false;
      }
    });
  }
}
