/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ParkingTestModule } from '../../../test.module';
import { ParkingPlaceComponent } from 'app/entities/parking-place/parking-place.component';
import { ParkingPlaceService } from 'app/entities/parking-place/parking-place.service';
import { ParkingPlace } from 'app/shared/model/parking-place.model';

describe('Component Tests', () => {
    describe('ParkingPlace Management Component', () => {
        let comp: ParkingPlaceComponent;
        let fixture: ComponentFixture<ParkingPlaceComponent>;
        let service: ParkingPlaceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ParkingTestModule],
                declarations: [ParkingPlaceComponent],
                providers: []
            })
                .overrideTemplate(ParkingPlaceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParkingPlaceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParkingPlaceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ParkingPlace(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.parkingPlaces[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
