/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParkingTestModule } from '../../../test.module';
import { ParkingPlaceDetailComponent } from 'app/entities/parking-place/parking-place-detail.component';
import { ParkingPlace } from 'app/shared/model/parking-place.model';

describe('Component Tests', () => {
    describe('ParkingPlace Management Detail Component', () => {
        let comp: ParkingPlaceDetailComponent;
        let fixture: ComponentFixture<ParkingPlaceDetailComponent>;
        const route = ({ data: of({ parkingPlace: new ParkingPlace(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ParkingTestModule],
                declarations: [ParkingPlaceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ParkingPlaceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParkingPlaceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.parkingPlace).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
