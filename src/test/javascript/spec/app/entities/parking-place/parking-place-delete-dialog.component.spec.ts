/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ParkingTestModule } from '../../../test.module';
import { ParkingPlaceDeleteDialogComponent } from 'app/entities/parking-place/parking-place-delete-dialog.component';
import { ParkingPlaceService } from 'app/entities/parking-place/parking-place.service';

describe('Component Tests', () => {
    describe('ParkingPlace Management Delete Component', () => {
        let comp: ParkingPlaceDeleteDialogComponent;
        let fixture: ComponentFixture<ParkingPlaceDeleteDialogComponent>;
        let service: ParkingPlaceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ParkingTestModule],
                declarations: [ParkingPlaceDeleteDialogComponent]
            })
                .overrideTemplate(ParkingPlaceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParkingPlaceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParkingPlaceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
