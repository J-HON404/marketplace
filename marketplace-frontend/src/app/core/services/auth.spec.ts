import { TestBed } from '@angular/core/testing';
import { TokenService } from './token.service'; // Nome corretto

describe('TokenService', () => { // Nome descrittivo corretto
  let service: TokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenService]
    });
    service = TestBed.inject(TokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});