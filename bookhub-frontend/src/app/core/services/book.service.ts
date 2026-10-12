import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class BookService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/books';

  getAll(page = 0, size = 20): Observable<Page<Book>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<Book>>(this.apiUrl, { params });
  }
}
