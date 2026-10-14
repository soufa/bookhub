import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BookService } from './book.service';

describe('BookService', () => {
  let service: BookService;
  let httpMock: HttpTestingController;

  const API = 'http://localhost:8080/api/books';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(BookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch books with pagination', () => {
    const fakePage = {
      content: [],
      totalElements: 0,
      totalPages: 0,
      number: 0,
      size: 20
    };

    service.getAll(0, 20).subscribe(res => {
      expect(res).toEqual(fakePage);
    });

    const req = httpMock.expectOne(r =>
      r.url === API && r.params.get('page') === '0' && r.params.get('size') === '20'
    );
    expect(req.request.method).toBe('GET');
    req.flush(fakePage);
  });
});
