/**  js-day6 
 *   유효성 검증 통과할 때만 `success.html` url로 이동합니다.
	1) 이름,이메일은 반드시 입력해야 함.
	2) 패스워드는 4글자 이상이어야 함.
	3) 나이는 15~99 사이 값.
 */

    let regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/;
	let regPassword =/^[a-zA-Z\\d`~!@#$%^&*()-_=+]{8,24}$/

function func_join() {
    const frm = document.forms[0]
    const id = frm.id
    const name = frm.name
    const email = frm.email
    const password = frm.password
    const age = frm.age
    let isValid = true
	
	if(id.value=='') {
        alert('아이디는 필수 입력입니다.')
        id.focus()
        isValid=false
    }else
    if(name.value=='') {
        alert('이름은 필수 입력입니다.')
        name.focus()
        isValid=false
    } else

    if(email.value=='') {
        alert('이메일은 필수 입력입니다.')
        email.focus()
        isValid=false
    } else

	//4)이메일은 기호 @ 와 . 을 포함해야 합니다.  .은 마지막 위치 아니어야 합니다.
	//  -> 잘못된 이메일형식체크
	if(email.value.indexOf('@')==-1 || email.value.indexOf('.') == -1 ||
	  email.value.endsWith('.') ) {
		alert('이메일 형식 체크 실패입니다.')
		email.focus()
		isValid=false
	} else

	//5)
    //실제 이메일 주소는 형식이 위의 조건보다 복잡합니다. 계정문자열에 특수기호는 - _ . 만 포함. 
    //                  도메인이름에 기호는 사용못합니다. naver.com O na-ver.com X  333naver.com X(숫자로 시작)
    //복잡한 조건의 유효성검사를 정규식표현으로 할수 있습니다.
    //정규표현식은 전화번호,이메일,패스워드, 한글,영문 입력체크에 활용.
    
    if(regEmail.test(email.value)===false) {		//test메소드는 정규표현식과 입력문자열 비교
        alert('정규표현식 검사 이메일 형식이 아닙니다.')
		email.focus()
		isValid=false
    }else 

/*
정규표현식은   / / 안에 작성. test메소드로 검사.
^ 는 시작지정
$ 는 끝지정
[] 는 [] 안의 문자들중 1개 선택 , [0-9a-zA-Z]는 숫자,영문소문자,영문대문자 중 1개
* 는 0번 이상 문자 반복
[]? 는 [] 안의 문자들이 있는가? 존재여부. 해당 문자가 있을수도 있고 없을수도 있다.
() 는 그룹
{n} 는 n개
{n,m}는 n개 이상, m번 이하
*/

//   if(regPassword.test(password.value)===false ) {

    if(password.value.length < 4 ) {
        alert('패스워드는 4글자 이상입니다.')
        password.focus()
        isValid=false
    }else
    
    if(age.value <= 15 || age.value >= 99) {
        alert('나이는 15~99 범위의 값이어야 합니다.')
        age.focus()
        isValid=false
    } 

	//6) 취미를 반드시 1개 이상 선택하기
	let cnt=0			//취미 체크한 개수 카운트
//	console.log(frm.hobby)
	frm.hobby.forEach(function(element){		//입력요소 배열에 대해 forEach
		if(element.checked) {
			console.log(element)
			cnt++
		}	
	})
	
	
//	console.log("선택한 취미 개수 : " + cnt)
	if(cnt==0) {
		alert('취미는 1개 이상 선택해 주세요.')
		isValid=false
	}

    if(isValid) {   //유효성 통과하여 제출합니다. 
        frm.submit()
    }
    
/*    else {
        alert('유효성 통과 실패!!!')
    }
*/
}