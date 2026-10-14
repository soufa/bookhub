import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { BookListComponent } from './book-list.component';
import { BookService } from '../../../core/services/book.service';

describe('BookListComponent', () => {
  let component: BookListComponent;
  let fixture: ComponentFixture<BookListComponent>;
  let bookSpy: jasmine.SpyObj<BookService>;

  const fakePage = {
    content: [
      { id: 1, title: 'Effective Java', author: 'Joshua Bloch',
        isbn: { value: '9780134685991' },
        price: { amount: 45, currency: 'EUR' },
        status: 'AVAILABLE' }
    ],
    totalElements: 1, totalPages: 1, number: 0, size: 20
  };

  beforeEach(async () => {
    bookSpy = jasmine.createSpyObj('BookService', ['getAll']);
    bookSpy.getAll.and.returnValue(of(fakePage));

    await TestBed.configureTestingModule({
      imports: [BookListComponent],
      providers: [
        { provide: BookService, useValue: bookSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BookListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should load books on init', () => {
    fixture.detectChanges();

    expect(bookSpy.getAll).toHaveBeenCalled();
    expect(component.books.length).toBe(1);
    expect(component.books[0].title).toBe('Effective Java');
    expect(component.total).toBe(1);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when loading books', () => {
    bookSpy.getAll.and.returnValue(
      throwError(() => ({ status: 500, message: 'Server error' }))
    );
    fixture.detectChanges();

    expect(component.error).toContain('500');
    expect(component.loading).toBeFalse();
  });

  it('should render book titles in the DOM', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Effective Java');
    expect(compiled.textContent).toContain('Joshua Bloch');
  });
});
