import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaffComponent} from './caff.component';

describe('CaffComponent', () => {
  let component: CaffComponent;
  let fixture: ComponentFixture<CaffComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaffComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
