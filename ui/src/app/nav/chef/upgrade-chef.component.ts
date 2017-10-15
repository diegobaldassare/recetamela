import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ChefRequest} from "../../shared/models/chef-request";
import {MediaService} from "../../shared/services/media.service";
import {UserService} from "../../shared/services/user.service";
import {ToasterService} from "angular2-toaster";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-upgrade-chef',
  templateUrl: './upgrade-chef.component.html',
  styleUrls: ['./upgrade-chef.component.css']
})
export class UpgradeChefComponent implements OnInit {

  private textChefForm: FormGroup;
  private chefRequest: ChefRequest = new ChefRequest();
  private image: Media;
  private uploadingCertificate: boolean;

  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private fb: FormBuilder,
              private mediaService: MediaService,
              private userService: UserService,
              private toaster: ToasterService){
    this.textChefForm = fb.group({
      'resumeChef': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit(): void {}

  postChefRequest() {
    this.chefRequest.resume = this.textChefForm.value.resumeChef;
    this.chefRequest.certificate = this.image;
    this.userService.postChefRequest(this.chefRequest).then(() => {
        this.toaster.pop("success", "Solicitud enviada correctamente");
      }, () => { this.toaster.pop("error", "No se pudo enviar"); }
    );
  }

  public addCertificate(e: Event) {
    e.preventDefault();
    this.uploadingCertificate = true;
    const files = (<HTMLInputElement> document.getElementById('certificate')).files;
    if (!files.length) return;
    this.mediaService.uploadMedia(files[0]).then(media => {
      this.uploadingCertificate = false;
      (<HTMLInputElement> document.getElementById('certificate')).value = '';
      this.image = media;
    }, () => { (<HTMLInputElement> document.getElementById('certificate')).value = ''; });
  }

  public get imageButton(): string {
    if (this.uploadingCertificate) return 'Subiendo';
    else return 'Agregar';
  }

  public removeCertificate() {
    this.chefRequest.certificate = null;
    this.image = null;
  }

  private disableButton(): boolean {
    return (!this.textChefForm.valid && this.image != null);
  }

}


