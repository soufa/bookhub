export interface Book {
  id: number;
  title: string;
  author: string;
  isbn: { value: string };
  price: { amount: number; currency: string };
  status: string;
}
